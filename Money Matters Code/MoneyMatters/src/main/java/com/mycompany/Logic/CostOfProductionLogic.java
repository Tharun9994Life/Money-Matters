package com.mycompany.Logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.mycompany.Records.FactorInput;
import com.mycompany.Repository.SQLQueriesFactorInput;
import com.mycompany.Users.Producer;

public class CostOfProductionLogic {

    // Calculates cumulative marginal costs for a given producer
    public static LinkedHashMap<Integer, Double> getCumulativeMarginalCosts(Producer producer) {
        ArrayList<Double> allMarginalCosts = SQLQueriesFactorInput.getAllMarginalCosts(producer);
        LinkedHashMap<Integer, Double> cumulativeMarginalCosts = new LinkedHashMap<>();
        double currentCosts = 0;

        for (int i = 0; i < allMarginalCosts.size(); i++) {
            currentCosts += allMarginalCosts.get(i);
            cumulativeMarginalCosts.put(i + 1, currentCosts);
        }

        return cumulativeMarginalCosts;
    }

    // Calculates cumulative marginal revenue for a given producer
    public static LinkedHashMap<Integer, Double> getCumulativeMarginalRevenue(Producer producer) {
        ArrayList<Double> allMarginalRevenue = SQLQueriesFactorInput.getAllMarginalRevenue(producer);
        LinkedHashMap<Integer, Double> cumulativeMarginalRevenue = new LinkedHashMap<>();
        double currentRevenue = 0;

        for (int i = 0; i < allMarginalRevenue.size(); i++) {
            currentRevenue += allMarginalRevenue.get(i);
            cumulativeMarginalRevenue.put(i + 1, currentRevenue);
        }

        return cumulativeMarginalRevenue;
    }

    // Generates all possible combinations of factor inputs
    public static ArrayList<ArrayList<FactorInput>> generateCombinations(ArrayList<FactorInput> factorInputs) {
        ArrayList<ArrayList<FactorInput>> allCombos = new ArrayList<>();
        
        int n = factorInputs.size();

        for (int size = 1; size <= n; size++) {
            combine(factorInputs, allCombos, new ArrayList<>(), 0, size);
        }

        return allCombos;
    }

    // Generate combinations of a specific size
    private static void combine(ArrayList<FactorInput> factorInputs, ArrayList<ArrayList<FactorInput>> allCombos, ArrayList<FactorInput> current, int start, int size) {
        if (current.size() == size) {
            allCombos.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < factorInputs.size(); i++) {
            current.add(factorInputs.get(i));
            combine(factorInputs, allCombos, current, i + 1, size);
            current.remove(current.size() - 1);
        }
    }

    // Calculates total profit for a given combination of factor inputs
    public static double totalProfit(ArrayList<FactorInput> combo) {
        double profit = 0;
        for (FactorInput factorInput : combo) {
            profit += factorInput.getMarginalProfit();
        }
        return profit;
    }

    // Calculates total cost for a given combination of factor inputs
    public static double totalCost(ArrayList<FactorInput> combo) {
        double cost = 0;
        for (FactorInput factorInput : combo) {
            cost += factorInput.getMarginalCost();
        }
        return cost;
    }

    // Determines the best combination of factor inputs that maximises profit within a cost constraint
    public static ArrayList<FactorInput> showBestCombination(ArrayList<FactorInput> allFactorInputsArrayList, double maxCost) {
        ArrayList<ArrayList<FactorInput>> allCombos = generateCombinations(allFactorInputsArrayList);

        double bestProfit = Double.NEGATIVE_INFINITY;
        ArrayList<FactorInput> bestCombo = new ArrayList<>();

        // Evaluate each combination
        for (ArrayList<FactorInput> combo : allCombos) {
            double cost = totalCost(combo);

            // Only consider combinations within the max cost constraint
            if (cost <= maxCost) {
                double profit = totalProfit(combo);

                // Update the best combination if profit is higher
                if (profit > bestProfit) {
                    bestProfit = profit;
                    bestCombo = combo;
                }
            }
        }

        return bestCombo;
    }
}