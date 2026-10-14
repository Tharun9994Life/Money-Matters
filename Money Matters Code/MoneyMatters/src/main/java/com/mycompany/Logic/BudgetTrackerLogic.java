package com.mycompany.Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mycompany.PythonFlaskAPI.APIQueue;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Records.OutlierGoods;
import com.mycompany.Records.Purchase;
import com.mycompany.Repository.SQLQueriesBudgetTracker;
import com.mycompany.Repository.SQLQueriesConsumers;
import com.mycompany.Users.Consumer;

public class BudgetTrackerLogic {
    private static final APIQueue taskManager = new APIQueue(3);
    private static final SQLQueriesConsumers SQLQueriesConsumers = new SQLQueriesConsumers(); 

    // Updates the consumer's next budget renewal date based on their renewal frequency
    public static void saveNextRenewedDate(Consumer consumer) {
        LocalDate lastRenewed = consumer.getLastRenewed();
        LocalDate nextRenewed;
        String frequencyOfBudgetRenewal = consumer.getFrequencyOfBudgetRenewal();

        // Determine next renewal date based on frequency
        switch (frequencyOfBudgetRenewal) {
            case "Daily":
                nextRenewed = LocalDate.now().plusDays(1);
                break;
            case "Weekly":
                nextRenewed = LocalDate.now().plusWeeks(1);
                break;
            case "Monthly":
                nextRenewed = LocalDate.now().plusMonths(1);
                break;
            default:
                nextRenewed = LocalDate.now().plusDays(1);
                break;
        }

        // If today is past the last renewal, update budget and renewal date in database
        if (LocalDate.now().equals(lastRenewed) || LocalDate.now().isAfter(lastRenewed)) {
            SQLQueriesConsumers.updateCurrentBudget(consumer.getBudgetSize(), consumer);
            SQLQueriesConsumers.updateLastRenewed(nextRenewed, consumer);
        }
    }

    // Reduces the total cost of a purchase from the current budget
    public static double reduceCurrentBudget(double currentBudget, int goodQuantity, double goodPrice) {
        return currentBudget - (goodQuantity * goodPrice);
    }

    // Returns a mapping of the last 7 days to total purchase prices
    public static LinkedHashMap<LocalDate, Double> getDateTotalPricePairs(Consumer consumer) {
        LinkedHashMap<LocalDate, Double> dateTotalPricePairs = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            int daysAgo = i;
            ArrayList<ArrayList<Double>> totalPriceOfPurchasesLastWeek = SQLQueriesBudgetTracker.getLastWeekPurchases(consumer, i);

            taskManager.submitTask(() -> {
                if (!totalPriceOfPurchasesLastWeek.isEmpty()) {
                    dateTotalPricePairs.put(LocalDate.now().minusDays(daysAgo), PythonFlaskAPI.getLastWeekPurchases(totalPriceOfPurchasesLastWeek));
                } 
                
                else {
                    dateTotalPricePairs.put(LocalDate.now().minusDays(daysAgo), 0.0);
                }
            });
        }

        return dateTotalPricePairs;
    }

    // Calculates mean of a list of doubles
    public static double calculateMean(ArrayList<Double> values) {
        double sum = 0.0;
        for (double val : values){
            sum += val;
        }

        return sum / values.size();
    }

    // Calculates variance given a mean
    public static double calculateVariance(ArrayList<Double> values, double mean) {
        double varianceSum = 0.0;
        for (double val : values) {
            varianceSum += (val - mean) * (val - mean);
        }

        return varianceSum / values.size();
    }

    // Calculates standard deviation of a list of doubles
    public static double calculateStdDev(ArrayList<Double> values) {
        if (values.isEmpty()) return 0.0;

        double mean = calculateMean(values);
        double variance = calculateVariance(values, mean);

        return Math.sqrt(variance);
    }

    // Groups purchases by item name. Organises all purchases by product to analyse spending patterns and detect anomalies
    public static LinkedHashMap<String, ArrayList<Double>> groupPurchases(ArrayList<Purchase> purchases) {
        LinkedHashMap<String, ArrayList<Double>> totalsPerItem = new LinkedHashMap<>();

        // Iterates over each purchase made by the consumer
        for (Purchase purchase : purchases) {
            String item = purchase.getGoodName();
            totalsPerItem.putIfAbsent(item, new ArrayList<>());
            totalsPerItem.get(item).add(purchase.getTotalPrice());
        }

        // Return the mapping of items to their list of total purchase prices
        return totalsPerItem;
    }

    // Groups purchases by item name
    public static LinkedHashMap<String, Double> calculateVolatility(ArrayList<Purchase> purchases, LinkedHashMap<String, ArrayList<Double>> totalsPerItem) {
        LinkedHashMap<String, Double> volatility = new LinkedHashMap<>();

        // Iterate through each item and its list of total purchase prices
        for (Map.Entry<String, ArrayList<Double>> entry : totalsPerItem.entrySet()) {
            String item = entry.getKey();
            ArrayList<Double> totals = entry.getValue();

            // If there are no purchase prices recorded, set volatility to 0. Else, calculate standard deviation of the list of prices
            if (totals.isEmpty()) {
                volatility.put(item, 0.0);
            } 
            
            else {
                volatility.put(item, calculateStdDev(totals));
            }
        }

        return volatility;
    }

    // Detects outlier purchases using z-score (> 1.5 considered risky)
    public static ArrayList<OutlierGoods> calculateOutliers(LinkedHashMap<String, ArrayList<Double>> totalsPerItem, LinkedHashMap<String, Double> volatility) {
        ArrayList<OutlierGoods> outlierGoods = new ArrayList<>();

        // Iterate over each item and its list of purchase totals
        for (Map.Entry<String, ArrayList<Double>> entry : totalsPerItem.entrySet()) {
            String goodName = entry.getKey();
            ArrayList<Double> totals = entry.getValue();

            // Calculate mean of purchase totals for this item
            double mean = calculateMean(totals);

            // Retrieve standard deviation for this item
            double stdDev = volatility.get(goodName);

            for (double price : totals) {
                double z = 0;
                try {
                    z = (price - mean) / stdDev;
                }

                // Division by 0 error
                catch (Exception ex) {
                    z = 0;
                }

                finally {
                    // Mark as outlier if z-score exceeds threshold
                    if (Math.abs(z) > 1.5) {
                        outlierGoods.add(new OutlierGoods(goodName, price, z));
                    }
                }
            }
        }

        return outlierGoods;
    }

    // Fetches outliers for a consumer based on their purchase history
    public static ArrayList<OutlierGoods> getOutliers(Consumer consumer) {
        ArrayList<Purchase> purchases = SQLQueriesBudgetTracker.getConsumerPurchase(consumer);
        LinkedHashMap<String, ArrayList<Double>> totalsPerItem = groupPurchases(purchases);
        LinkedHashMap<String, Double> volatility = calculateVolatility(purchases, totalsPerItem);
        return calculateOutliers(totalsPerItem, volatility);
    }
}