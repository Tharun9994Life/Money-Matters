import math
import numpy as np
from datetime import date

class BudgetTrackerMaths:
    @staticmethod
    def calculateIncomeComponents(budget):
        # Split the budget into essentials, leisure, and savings
        # Essentials = 45%, Leisure = 30%, Savings = 25%
        desiredIncomeComponents = {}
        
        essentials = budget * 0.45
        leisure = budget * 0.3
        savings = budget * 0.25

        desiredIncomeComponents.update({"Essentials": essentials})
        desiredIncomeComponents.update({"Leisure": leisure})
        desiredIncomeComponents.update({"Savings": savings})
        
        return desiredIncomeComponents

    @staticmethod
    def futureBudget(incomeComponents, growthRate): 
        # Convert percentage growth rate to decimal
        growthRate = growthRate / 100
        
        # Calculate equivalent monthly growth rate
        monthlyGrowthRate = (1 + growthRate) ** (1/12) - 1
        
        # Convert income components into numpy array
        incomeComponents = np.array(list(incomeComponents.values()))
        
        # Create months 0-11 as a column vector
        months = np.arange(0, 12).reshape(-1, 1)
        
        # Growth factor for each month
        growthRates = (1 + monthlyGrowthRate) ** months
        
        # Project income components over 12 months
        futureBudgets = incomeComponents * growthRates
        
        return futureBudgets
    
    @staticmethod
    def sustainableSpendingPerDay(essentials): 
        # Divide essential expenses equally across 30 days
        return essentials / 30
    
    @staticmethod
    def calculateBudgetOverAWeek(prices, quantity):
        # Convert lists into numpy arrays
        pricesMatrix = np.array(prices)
        quantitiesMatrix = np.array(quantity)
        
        # Multiply prices by quantities and sum for total weekly cost
        return (pricesMatrix * quantitiesMatrix).sum()