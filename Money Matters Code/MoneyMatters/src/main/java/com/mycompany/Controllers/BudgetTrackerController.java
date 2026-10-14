package com.mycompany.Controllers;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Algorithms.MergeSort;
import com.mycompany.EconomicFigures.MacroIndicatorFetcher;
import com.mycompany.EconomicFigures.MacroIndicators;
import com.mycompany.Logic.BudgetTrackerLogic;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Records.GoodRecord;
import com.mycompany.Records.Purchase;
import com.mycompany.Repository.SQLQueriesBudgetTracker;
import com.mycompany.Repository.SQLQueriesConsumers;
import com.mycompany.Repository.SQLQueriesGoods;
import com.mycompany.Repository.SQLQueriesPurchase;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class BudgetTrackerController {

    private static final SQLQueriesConsumers SQLQueriesConsumers = new SQLQueriesConsumers(); 

    public static ModelAndView showData(ModelAndView mv, Consumer consumer) {
        mv.addObject("budgetSize", consumer.getBudgetSize());
        mv.addObject("currentBudget", consumer.getCurrentBudget());
        mv.addObject("frequencyOfBudgetRenewal", consumer.getFrequencyOfBudgetRenewal());

        // Calculate income distribution (Essentials, Leisure, Savings)
        LinkedHashMap<String, Double> incomeComponents = PythonFlaskAPI.calculateIncomeComponents(consumer.getBudgetSize());
        mv.addObject("incomeComponents", incomeComponents);

        if (consumer.getFrequencyOfBudgetRenewal().equals("Monthly")) {
            mv.addObject("sustainableSpending", PythonFlaskAPI.sustainableSpending(incomeComponents.get("Essentials")));
        }

        // Fetch UK inflation data to project consumer’s future budget
        MacroIndicators UKInflationRate = MacroIndicatorFetcher.instantiateIndicator("GBR", "FP.CPI.TOTL.ZG");
        mv.addObject("futureBudget", PythonFlaskAPI.futureBudget(incomeComponents, UKInflationRate.getData().get(Year.now().minusYears(1)).doubleValue()));

        mv.addObject("todaySpending", SQLQueriesPurchase.showTodaysSpending(consumer));

        // Display future budget over the next 12 months
        ArrayList<String> upcomingMonthsArrayList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.now().plusMonths(i);
            upcomingMonthsArrayList.add(date.format(df));
        }

        mv.addObject("months", upcomingMonthsArrayList);

        // Add spending trend data and outlier purchases
        mv.addObject("dateTotalPricePairs", BudgetTrackerLogic.getDateTotalPricePairs(consumer));
        mv.addObject("riskyPurchases", BudgetTrackerLogic.getOutliers(consumer));

        return mv;
    }

    @GetMapping("budget-tracker")
    public ModelAndView showBudgetTracker(HttpSession session) {
        ModelAndView mv = new ModelAndView("budgettracker/budget-tracker");

        if (session.getAttribute("user") instanceof Consumer consumer) {
            showData(mv, consumer);

            // Save next renewal date for the budget
            BudgetTrackerLogic.saveNextRenewedDate(consumer);

            // Fetch and sort purchases (latest first)
            ArrayList<Purchase> allConsumerPurchases = SQLQueriesBudgetTracker.getConsumerPurchase(consumer);
            MergeSort.mergeSortPurchasesByDate(allConsumerPurchases);
            Collections.reverse(allConsumerPurchases);
            mv.addObject("consumerPurchases", allConsumerPurchases);
        }

        // If logged in as a Producer
        else if (session.getAttribute("user") instanceof Producer producer) {
            mv.setViewName("userdashboard/user-dashboard");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Unable To Access. Need To Be A Consumer.");
        }

        // If not logged in
        else {
            mv.setViewName("login/start");
        }

        return mv;
    }

    @PostMapping("budget-tracker")
    public ModelAndView storeBudgetData(@RequestParam String formName, 
                @RequestParam (required = false) Double budgetSize,
                @RequestParam (required = false) String frequencyOfRenewal,
                @RequestParam (required = false) String goodName,
                @RequestParam (required = false) Double goodPrice,
                @RequestParam (required = false) Integer goodQuantity,
                @RequestParam (required = false) String sort,
                HttpSession session) {

        ModelAndView mv = new ModelAndView("redirect:budget-tracker");

        if (session.getAttribute("user") instanceof Consumer consumer) {

            // Update budget details
            switch (formName) {
                case "userBudget":
                    if (budgetSize != null) {
                        SQLQueriesConsumers.updateBudgetSize(budgetSize, consumer);
                        SQLQueriesConsumers.updateCurrentBudget(budgetSize, consumer);
                        SQLQueriesConsumers.updateFrequencyOfBudgetRenewal(frequencyOfRenewal, consumer);
                        SQLQueriesConsumers.updateLastRenewed(LocalDate.now(), consumer);
                        
                        // Update sustainable spending based on new budget
                        LinkedHashMap<String, Double> incomeComponents = PythonFlaskAPI.calculateIncomeComponents(consumer.getBudgetSize());
                        SQLQueriesConsumers.updateSustainableSpending(PythonFlaskAPI.sustainableSpending(incomeComponents.get("Essentials")), consumer);
                    }   
                    break;

                case "userGoods":
                    double currentBudget = consumer.getCurrentBudget();
                    double totalCost = goodPrice * goodQuantity;

                    // Prevent purchase if insufficient budget
                    if (currentBudget < totalCost) {
                        mv.setViewName("redirect:budget-tracker");
                        mv.addObject("windowsAlert", true);
                        mv.addObject("windowsAlertMessage", "Insufficient budget for this purchase!");
                        return mv;
                    }

                    if (!SQLQueriesGoods.doesGoodIDExist(goodName, goodPrice)) {
                        SQLQueriesGoods.insertGood(goodName, goodPrice);
                    }

                    GoodRecord good = new GoodRecord(SQLQueriesGoods.getGoodID(goodName, goodPrice), goodName, goodPrice);
                    SQLQueriesPurchase.insertPurchase(consumer, good, goodQuantity);
                    currentBudget -= totalCost;

                    SQLQueriesConsumers.updateCurrentBudget(currentBudget, consumer);
                    consumer.setCurrentBudget(currentBudget);
                    break;

                case "filterBudget":
                    mv.setViewName("budgettracker/budget-tracker");
                    ArrayList<Purchase> allConsumerPurchases = SQLQueriesBudgetTracker.getConsumerPurchase(consumer);
                    switch (sort) {
                        case "date (latest)":
                            MergeSort.mergeSortPurchasesByDate(allConsumerPurchases);
                            Collections.reverse(allConsumerPurchases);
                            break;
                        case "date (earliest)":
                            MergeSort.mergeSortPurchasesByDate(allConsumerPurchases);
                            break;
                        case "most expensive":
                            MergeSort.mergeSortPurchasesByPrice(allConsumerPurchases);
                            Collections.reverse(allConsumerPurchases);
                            break;
                        case "least expensive":
                            MergeSort.mergeSortPurchasesByPrice(allConsumerPurchases);
                            break;
                        case "most bought":
                            MergeSort.mergeSortPurchasesByQuantity(allConsumerPurchases);
                            Collections.reverse(allConsumerPurchases);
                            break;
                        case "least bought":
                            MergeSort.mergeSortPurchasesByQuantity(allConsumerPurchases);
                            break;   
                        default:
                            break;
                    }
                        
                    mv.addObject("selectedSort", sort);
                    mv.addObject("consumerPurchases", allConsumerPurchases);
                    break;

                default:
                    break;
            }

            consumer = SQLQueriesConsumers.getData(consumer.getConsumerID());
            session.setAttribute("user", consumer);
            showData(mv, consumer);
        }

        return mv;
    }
}