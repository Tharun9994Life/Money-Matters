package com.mycompany.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Logic.InvestorLogic;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Repository.SQLQueriesConsumers;
import com.mycompany.Repository.SQLQueriesProducers;
import com.mycompany.Repository.SQLQueriesStocks;
import com.mycompany.Repository.SQLQueriesTrade;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class InvestorController {

    private static final SQLQueriesConsumers SQLQueriesConsumers = new SQLQueriesConsumers(); 
    private static final SQLQueriesProducers SQLQueriesProducers = new SQLQueriesProducers(); 

    public ModelAndView showData(ModelAndView mv, HttpSession session) {
        // Updates and inserts all stocks data to the database
        SQLQueriesStocks.insertAllStock();

        mv.addObject("allTickerInfo", InvestorLogic.getAllTickerInfo());
    
        // Time ranges for candlestick chart
        LinkedHashMap<String, String> timePeriods = new LinkedHashMap<>();
        timePeriods.put("3 Months Ago", LocalDate.now().minusMonths(3).toString());
        timePeriods.put("6 Months Ago", LocalDate.now().minusMonths(6).toString());
        timePeriods.put("1 Year Ago", LocalDate.now().minusYears(1).toString());
        timePeriods.put("5 Years Ago", LocalDate.now().minusYears(5).toString());
        timePeriods.put("10 Years Ago", LocalDate.now().minusYears(10).toString());
        mv.addObject("timePeriods", timePeriods);

        // If user is a Consumer
        if (session.getAttribute("user") instanceof Consumer consumer) {
            SQLQueriesConsumers.needToUpdateStockBudget(consumer);
            mv.addObject("stockBudget", consumer.getStockBudget());

            // Owned stocks
            ArrayList<String> stockNames = new ArrayList<>();
            ArrayList<Integer> netQuantities = new ArrayList<>();
            ArrayList<Double> totalAskPrices = new ArrayList<>();
            ArrayList<Double> averageAskPrices = new ArrayList<>();
            ArrayList<Double> currentAskPrices = new ArrayList<>();
            ArrayList<Double> potentialProfit = new ArrayList<>();

            // Retrieve user's stock
            for (ArrayList<Object> row : SQLQueriesTrade.getCurrentUserStock(consumer, null)) {
                stockNames.add((String) row.get(0));
                netQuantities.add(((Number) row.get(1)).intValue());
                totalAskPrices.add(((Number) row.get(2)).doubleValue());
                averageAskPrices.add(((Number) row.get(3)).doubleValue());
                currentAskPrices.add(PythonFlaskAPI.getAskPrice(InvestorLogic.getAllTickerInfo().get((String) row.get(0))));
                potentialProfit.add(((Number) row.get(4)).doubleValue());
            }

            mv.addObject("allStockNames", stockNames);
            mv.addObject("allNetQuantities", netQuantities);
            mv.addObject("allTotalAskPrices", totalAskPrices);
            mv.addObject("allAverageAskPrices", averageAskPrices);
            mv.addObject("allCurrentAskPrices", currentAskPrices);
            mv.addObject("allPotentialProfit", potentialProfit);

            // Update user data
            consumer = SQLQueriesConsumers.getData(consumer.getConsumerID());
            session.setAttribute("user", consumer);
        }

        // If user is a Producer
        else if (session.getAttribute("user") instanceof Producer producer) {
            SQLQueriesProducers.needToUpdateStockBudget(producer);
            mv.addObject("stockBudget", producer.getStockBudget());

            ArrayList<String> stockNames = new ArrayList<>();
            ArrayList<Integer> netQuantities = new ArrayList<>();
            ArrayList<Double> totalAskPrices = new ArrayList<>();
            ArrayList<Double> averageAskPrices = new ArrayList<>();
            ArrayList<Double> currentAskPrices = new ArrayList<>();
            ArrayList<Double> potentialProfit = new ArrayList<>();

            // Retrieve user's stock
            for (ArrayList<Object> row : SQLQueriesTrade.getCurrentUserStock(null, producer)) {
                stockNames.add((String) row.get(0));
                netQuantities.add(((Number) row.get(1)).intValue());
                totalAskPrices.add(((Number) row.get(2)).doubleValue());
                averageAskPrices.add(((Number) row.get(3)).doubleValue());
                currentAskPrices.add(PythonFlaskAPI.getAskPrice(InvestorLogic.getAllTickerInfo().get((String) row.get(0))));
                potentialProfit.add(((Number) row.get(4)).doubleValue());
            }

            mv.addObject("allStockNames", stockNames);
            mv.addObject("allNetQuantities", netQuantities);
            mv.addObject("allTotalAskPrices", totalAskPrices);
            mv.addObject("allAverageAskPrices", averageAskPrices);
            mv.addObject("allCurrentAskPrices", currentAskPrices);
            mv.addObject("allPotentialProfit", potentialProfit);

            // Update user data
            producer = SQLQueriesProducers.getData(producer.getProducerID());
            session.setAttribute("user", producer);
        }

        return mv;
    }

    @GetMapping("investor")
    public ModelAndView showInvestorCentre(HttpSession session) {
        ModelAndView mv = new ModelAndView("investor/investor");

        // Only logged-in Consumers or Producers can access
        if (session.getAttribute("user") instanceof Consumer consumer || session.getAttribute("user") instanceof Producer producer) {
            showData(mv, session);
        }

        else {
            mv.setViewName("login/start");
        }

        return mv;
    }

    @PostMapping("investor")
    public ModelAndView postInvestorCentre(@RequestParam String formName,
                        @RequestParam(required = false) String tickerSymbol,
                        @RequestParam(required = false) String timeSpan,
                        @RequestParam(required = false) Integer quantityBuy, 
                        @RequestParam(required = false) Integer quantitySell, 
                        HttpSession session) {
                
        ModelAndView mv = new ModelAndView("investor/investor");

        // Logic for Consumers and Producers
        if (session.getAttribute("user") instanceof Consumer consumer || session.getAttribute("user") instanceof Producer producer) {

            // If user selects a company, load chart and load details
            if (formName.equals("selectCompany")) {
                mv.addObject("companyInformation", InvestorLogic.getCompanyInfo(tickerSymbol));
                mv.addObject("stockPriceComponents", InvestorLogic.getStockPriceDataComponents(tickerSymbol, timeSpan));
                mv.addObject("companyName", InvestorLogic.getAllTickerInfoReversed().get(tickerSymbol));
                mv.addObject("askPrice", PythonFlaskAPI.getAskPrice(tickerSymbol));
                session.setAttribute("currentTickerSymbol", tickerSymbol);

                // SMA Information
                LinkedHashMap<String, Object> SMA10Day = PythonFlaskAPI.calculateSMA(tickerSymbol, 10);
                LinkedHashMap<String, Object> SMA50Day = PythonFlaskAPI.calculateSMA(tickerSymbol, 50);
                LinkedHashMap<String, Object> SMA100Day = PythonFlaskAPI.calculateSMA(tickerSymbol, 100);

                mv.addObject("SMA10DayTrend", SMA10Day.get("trend"));
                mv.addObject("SMA10DayGradient", SMA10Day.get("gradient"));
                mv.addObject("SMA50DayTrend", SMA50Day.get("trend"));
                mv.addObject("SMA50DayGradient", SMA50Day.get("gradient"));
                mv.addObject("SMA100DayTrend", SMA100Day.get("trend"));
                mv.addObject("SMA100DayGradient", SMA100Day.get("gradient"));

                mv.addObject("graphContainerDiv", "graphContainerDiv");
                mv.addObject("companyDetailsDiv", "companyDetailsDiv");
                mv.addObject("purchaseStockDiv", "purchaseStockDiv");
                mv.addObject("sellStockDiv", "sellStockDiv");
            }
        }

        // Trading logic for Consumers
        if (session.getAttribute("user") instanceof Consumer consumer) {
            String currentTickerSymbol = (String) session.getAttribute("currentTickerSymbol");

            if (formName.equals("purchaseStock")) {
                // Deduct cost from stock budget and insert buy trade. Updates the Database
                SQLQueriesConsumers.updateStockBudget(consumer, consumer.getStockBudget() - (quantityBuy * PythonFlaskAPI.getAskPrice(currentTickerSymbol)));
                SQLQueriesTrade.insertTrade(consumer, null, SQLQueriesStocks.getStockID(InvestorLogic.getAllTickerInfoReversed().get(currentTickerSymbol)), "BUY", PythonFlaskAPI.getAskPrice(currentTickerSymbol), quantityBuy);
                mv.setViewName("redirect:investor");
            }
            else if (formName.equals("sellStock")) {
                // Add profit to stock budget and insert sell trade. Updates the Database
                SQLQueriesConsumers.updateStockBudget(consumer, consumer.getStockBudget() + (quantitySell * PythonFlaskAPI.getAskPrice(currentTickerSymbol)));
                SQLQueriesTrade.insertTrade(consumer, null, SQLQueriesStocks.getStockID(InvestorLogic.getAllTickerInfoReversed().get(currentTickerSymbol)), "SELL", PythonFlaskAPI.getAskPrice(currentTickerSymbol), quantitySell);
                mv.setViewName("redirect:investor");
            }

            consumer = SQLQueriesConsumers.getData(consumer.getConsumerID());
            session.setAttribute("user", consumer);
        } 
        
        // Trading logic for Producers
        else if (session.getAttribute("user") instanceof Producer producer) {   
            String currentTickerSymbol = (String) session.getAttribute("currentTickerSymbol");
            
            if (formName.equals("purchaseStock")) {
                // Deduct cost from stock budget and insert buy trade. Updates the Database
                SQLQueriesProducers.updateStockBudget(producer, producer.getStockBudget() - (quantityBuy * PythonFlaskAPI.getAskPrice(currentTickerSymbol)));
                SQLQueriesTrade.insertTrade(null, producer, SQLQueriesStocks.getStockID(InvestorLogic.getAllTickerInfoReversed().get(currentTickerSymbol)), "BUY", PythonFlaskAPI.getAskPrice(currentTickerSymbol), quantityBuy);
                mv.setViewName("redirect:investor");
            }

            else if (formName.equals("sellStock")) {
                // Add profit to stock budget and insert sell trade. Updates the Database
                SQLQueriesProducers.updateStockBudget(producer, producer.getStockBudget() + (quantitySell * PythonFlaskAPI.getAskPrice(currentTickerSymbol)));
                SQLQueriesTrade.insertTrade(null, producer, SQLQueriesStocks.getStockID(InvestorLogic.getAllTickerInfoReversed().get(currentTickerSymbol)), "SELL", PythonFlaskAPI.getAskPrice(currentTickerSymbol), quantitySell);
                mv.setViewName("redirect:investor");
            }

            producer = SQLQueriesProducers.getData(producer.getProducerID());
            session.setAttribute("user", producer);
        }

        mv.addObject("selectedTickerSymbol", tickerSymbol);
        mv.addObject("selectedTimeSpan", timeSpan);
        showData(mv, session);
        
        return mv;
    }
}