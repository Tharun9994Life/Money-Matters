package com.mycompany.MoneyMatters;

import com.mycompany.Logic.CostOfProductionLogic;
import com.mycompany.Records.FactorInput;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.RepeatedTest;

public class CostRestraintTestRandom {
    
    final private Random randomNumberGenerator = new Random();

    private String randomStringGenerator() {
        final String uppercase = "qwertyuiopasdfghjklzxcvbnm".toUpperCase();
        final String lowercase = "qwertyuiopasdfghjklzxcvbnm".toLowerCase();
        final String numbers = "1234567890";
        final String availableCharacters = uppercase + lowercase + numbers;
        String randomString = "";
       
        for (int i = 0; i < randomNumberGenerator.nextInt(1, 30); i++) {
            randomString += availableCharacters.charAt(randomNumberGenerator.nextInt(0, availableCharacters.length()));
        }

        return randomString;
    }

    private ArrayList<FactorInput> randomFactorInputGenerator(int size) {
        ArrayList<FactorInput> factorInputs = new ArrayList<>();
        final String[] factorInputTypes = {"Capital", "Labour"};
       
        for (int i = 0; i < size; i++) {
            int randomID = randomNumberGenerator.nextInt(0, 1000000);
            String randomFactorInputName = randomStringGenerator();
            String randomFactorInputDescription = randomStringGenerator();
            double randomMarginalCost = randomNumberGenerator.nextDouble(0, 1000);
            double randomMarginalRevenue = randomNumberGenerator.nextDouble(0, 1000);

            factorInputs.add(new FactorInput(randomID, factorInputTypes[randomNumberGenerator.nextInt(0, 2)], randomFactorInputName, randomFactorInputDescription, randomMarginalCost, randomMarginalRevenue));
        }

        return factorInputs;
    }

    @RepeatedTest(3)
    public void maximiseProfitWithFixedCostRandom() {
        final ArrayList<FactorInput> factorInputs = randomFactorInputGenerator(5);
        final int maxCost = randomNumberGenerator.nextInt(0, 2500);
        ArrayList<FactorInput> bestFactorInputs = CostOfProductionLogic.showBestCombination(factorInputs, maxCost);
       
        System.out.println("All Workers:");
        for (int i = 0; i < factorInputs.size(); i++) {
            System.out.println(factorInputs.get(i).toString());
        }

        System.out.println("\nMax Cost Restraint: " + maxCost + "\n");

        System.out.println("Best Workers:");
        for (int i = 0; i < bestFactorInputs.size(); i++) {
            System.out.println(bestFactorInputs.get(i).toString());
        }
    }
}