package com.mycompany.Controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.mycompany.Logic.CostOfProductionLogic;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Records.FactorInput;
import com.mycompany.Repository.SQLQueriesFactorInput;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class CostOfProductionController {

    // Stack to store previous states of FactorInput for undo
    private final Stack<LinkedHashMap<String, FactorInput>> undoStack = new Stack<>();

    public ModelAndView showData(ModelAndView mv, Producer producer) {
        DecimalFormat df = new DecimalFormat("0.00");
        Integer numberOfLabour;
        Integer numberOfCapital;
        Double totalCost;
        Double averageCost;
        Double totalRevenue;
        Double averageRevenue;
        Double totalProfit;
        Double averageProfit;
        Double cobbDouglasFunction;
        
        // Fetch all factor inputs from a given producer
        LinkedHashMap<String, FactorInput> allFactorInputsLinkedHashMap = SQLQueriesFactorInput.getAllFactorInputTable(producer);

        try {
            // Fetch counts of labour and capital inputs
            numberOfLabour = SQLQueriesFactorInput.getNumberOfFactorInputs("Labour", producer);
            numberOfCapital = SQLQueriesFactorInput.getNumberOfFactorInputs("Capital", producer);

            // Fetch totals and averages
            totalCost = SQLQueriesFactorInput.getTotalCost(producer.getProducerID());
            averageCost = SQLQueriesFactorInput.getAverageCost(producer.getProducerID());
            totalRevenue = SQLQueriesFactorInput.getTotalRevenue(producer.getProducerID());
            averageRevenue = SQLQueriesFactorInput.getAverageRevenue(producer.getProducerID());
            totalProfit = SQLQueriesFactorInput.getTotalProfit(producer.getProducerID());
            averageProfit = SQLQueriesFactorInput.getAverageProfit(producer.getProducerID());

            // Perform Cobb-Douglas production function
            cobbDouglasFunction = Double.valueOf(PythonFlaskAPI.performCobbDouglasFunction(totalRevenue, totalCost, numberOfCapital, numberOfLabour));
        }

        catch (Exception ex) {
            numberOfLabour = 0;
            numberOfCapital = 0;
            totalCost = 0.0;
            averageCost = 0.0;
            totalRevenue = 0.0;
            averageRevenue = 0.0;
            totalProfit = 0.0;
            averageProfit = 0.0;
            cobbDouglasFunction = 0.0;
        }

        mv.addObject("numberOfLabour", numberOfLabour);
        mv.addObject("numberOfCapital", numberOfCapital);
        mv.addObject("totalCost", df.format(totalCost));
        mv.addObject("averageCost", df.format(averageCost));
        mv.addObject("totalRevenue", df.format(totalRevenue));
        mv.addObject("averageRevenue", df.format(averageRevenue));
        mv.addObject("totalProfit", df.format(totalProfit));
        mv.addObject("averageProfit", df.format(averageProfit));

        try {
            mv.addObject("cobbDouglasFunction", df.format(cobbDouglasFunction));
        }

        catch (Exception ex){
            System.out.println("Error when parsing the cobbDouglasFunction");
        }
        
        mv.addObject("allFactorInputs", allFactorInputsLinkedHashMap);
        mv.addObject("cumulativeMarginalCosts", CostOfProductionLogic.getCumulativeMarginalCosts(producer));    
        mv.addObject("cumulativeMarginalRevenue", CostOfProductionLogic.getCumulativeMarginalRevenue(producer));        
    
        return mv;
    }

    @GetMapping("cost-of-production")
    public ModelAndView showCostOfProduction(HttpSession session) {
        ModelAndView mv = new ModelAndView("costofproduction/cost-of-production");

        if (session.getAttribute("user") instanceof Producer producer) {
            showData(mv, producer);
        }

        else if (session.getAttribute("user") instanceof Consumer consumer) {
            mv.setViewName("userdashboard/user-dashboard");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Unable To Access. Need To Be A Producer.");
        }

        else {
            mv.setViewName("login/start");
        }

        return mv;
    }

    @PostMapping("cost-of-production")
    public ModelAndView getCostOfProduction(@RequestParam String formName, 
                        @RequestParam (required = false) String factorInputType,
                        @RequestParam (required = false) String factorInputName,
                        @RequestParam (required = false) String factorInputDescription,
                        @RequestParam (required = false) Double marginalCost, 
                        @RequestParam (required = false) Double marginalRevenue, 
                        @RequestParam (required = false) String factorInputDelete, 
                        @RequestParam (required = false) Double maximumCost, 
                        HttpSession session) {

        ModelAndView mv = new ModelAndView("costofproduction/cost-of-production");
        LinkedHashMap<String, FactorInput> tempLinkedHashMap = new LinkedHashMap<>();

        if (session.getAttribute("user") instanceof Producer producer) {
            
            // Insert new factor input
            if (formName.equals("factorInputInsert")) {
                FactorInput factorInput = new FactorInput(factorInputType, factorInputName, factorInputDescription, marginalCost, marginalRevenue);
                tempLinkedHashMap.put("Insert", factorInput);
                SQLQueriesFactorInput.insertDataIntoFactorInputTable(factorInput, producer);
                undoStack.add(tempLinkedHashMap);
            }

            // Update existing factor input (push previous state to undo stack)
            else if (formName.equals("factorInputUpdate")) {
                tempLinkedHashMap.put("Update", SQLQueriesFactorInput.getFactorInputByName(producer, factorInputName));
                SQLQueriesFactorInput.updateDataIntoFactorInputTable(marginalCost, marginalRevenue, factorInputName, producer);
                undoStack.add(tempLinkedHashMap);
            }

            // Delete factor input (save deleted record to undo stack)
            else if (formName.equals("factorInputDelete")) {
                tempLinkedHashMap.put("Delete", SQLQueriesFactorInput.getFactorInputByName(producer, factorInputDelete));
                SQLQueriesFactorInput.deleteDataFromFactorInputTable(factorInputDelete, producer);
                undoStack.add(tempLinkedHashMap);
            }

            // Compute optimal factor input combination under budget constraint
            else if (formName.equals("optimalFactorInput")) {
                LinkedHashMap<String, FactorInput> allFactorInputsLinkedHashMap = SQLQueriesFactorInput.getAllFactorInputTable(producer);
                ArrayList<FactorInput> allFactorInputsArrayList = new ArrayList<>(allFactorInputsLinkedHashMap.values());
                mv.addObject("bestCombination", CostOfProductionLogic.showBestCombination(allFactorInputsArrayList, maximumCost));
            }

            // Undo last change
            else if (formName.equals("undo")) {
                if (!undoStack.isEmpty()) {
                    Map<String, FactorInput> lastAction = undoStack.pop();
                    String action = lastAction.keySet().iterator().next();
                    FactorInput factorInput = lastAction.values().iterator().next();

                    switch (action) {
                        case "Update": // change to old cost/revenue values
                            SQLQueriesFactorInput.updateDataIntoFactorInputTable(factorInput.getMarginalCost(), factorInput.getMarginalRevenue(), factorInput.getFactorInputName(), producer);
                            break;
                        case "Insert": // remove the inserted record
                            SQLQueriesFactorInput.deleteDataFromFactorInputTable(factorInput.getFactorInputName(), producer);
                            break;
                        case "Delete": // restore deleted record
                            SQLQueriesFactorInput.insertDataIntoFactorInputTable(factorInput, producer);
                            break;
                        default:
                            break;
                    }
                }
                
                else {
                    // Stack is empty
                    mv.addObject("windowsAlert", true);
                    mv.addObject("windowsAlertMessage", "Stack is Empty, Nothing to undo");
                }
            }

            showData(mv, producer);
        }

        return mv;
    }   
}