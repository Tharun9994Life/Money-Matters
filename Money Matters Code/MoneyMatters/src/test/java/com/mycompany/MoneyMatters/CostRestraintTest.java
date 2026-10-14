package com.mycompany.MoneyMatters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.mycompany.Logic.CostOfProductionLogic;
import com.mycompany.Records.FactorInput;

class CostRestraintTest {

    @Test
    void maximiseProfitWithFixedCost() {
        ArrayList<FactorInput> factorInputs = new ArrayList<>();
        factorInputs.add(new FactorInput(1, "Labour", "Worker 1", "Construction", 100, 300));
        factorInputs.add(new FactorInput(2, "Labour", "Worker 2", "Construction", 150, 200));
        factorInputs.add(new FactorInput(3, "Labour", "Worker 3", "Construction", 200, 500));
        factorInputs.add(new FactorInput(4, "Capital", "Machine 1", "Construction", 120, 180));
        
        int maxCost = 300;

        ArrayList<FactorInput> bestCombination = CostOfProductionLogic.showBestCombination((ArrayList<FactorInput>) factorInputs, maxCost);
        Set<Integer> actualIDs = bestCombination.stream().map(FactorInput::getFactorInputID).collect(Collectors.toSet());
        
        Set<Integer> expectedIDs = Set.of(1, 3);
        assertEquals(expectedIDs, actualIDs);
    }

    @Test
    void testNoAffordableFactorInputs() {
        ArrayList<FactorInput> factorInputs = new ArrayList<>();

        factorInputs.add(new FactorInput(1, "Labour", "Worker 1", "Construction", 500, 1000));
        factorInputs.add(new FactorInput(2, "Capital", "Machine 1", "Construction", 600, 900));
        
        int maxCost = 100;
        
        ArrayList<FactorInput> bestCombination = CostOfProductionLogic.showBestCombination((ArrayList<FactorInput>) factorInputs, maxCost);
        assertTrue(bestCombination.isEmpty());
    }

    @Test
    void testSingleSelection() {
        ArrayList<FactorInput> factorInputs = new ArrayList<>();

        factorInputs.add(new FactorInput(1, "Labour", "Worker 1", "Construction", 400, 800));
        factorInputs.add(new FactorInput(2, "Capital", "Machine 1", "Construction", 100, 200));

        int maxCost = 400;

        ArrayList<FactorInput> bestCombination = CostOfProductionLogic.showBestCombination((ArrayList<FactorInput>) factorInputs, maxCost);
        Set<Integer> actualIDs = bestCombination.stream()
                .map(FactorInput::getFactorInputID)
                .collect(Collectors.toSet());

        Set<Integer> expectedIDs = Set.of(1);       
        assertEquals(expectedIDs, actualIDs);
    }

    @Test
    void testEmptyInput() {
        ArrayList<FactorInput> factorInputs = new ArrayList<>();
        int maxCost = 500;

        ArrayList<FactorInput> bestCombination = CostOfProductionLogic.showBestCombination((ArrayList<FactorInput>) factorInputs, maxCost);

        assertTrue(bestCombination.isEmpty());
    }
}