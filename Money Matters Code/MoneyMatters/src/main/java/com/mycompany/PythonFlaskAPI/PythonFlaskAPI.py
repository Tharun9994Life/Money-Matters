from flask import Flask, request, jsonify
from BudgetTrackerMaths import BudgetTrackerMaths
from CostOfProductionMaths import CostOfProductionMaths
from InvestmentData import InvestmentData
from OllamaAI import OllamaAI

FlaskAPI = Flask(__name__)

# Breakdown income into components (essential, leisure, savings)
@FlaskAPI.route("/calculate-Income-Components", methods=["POST"])
def calculateIncomeComponents():
    data = request.json
    income = data.get("income")
    incomeComponents = BudgetTrackerMaths.calculateIncomeComponents(income)
    return jsonify(incomeComponents)

# Calculates future budget based on income components and inflation
@FlaskAPI.route("/future-budget", methods=["POST"])
def futureBudget():
    data = request.json
    incomeComponents = data.get("incomeComponents")
    growthRate = data.get("growthRate")
    futureBudget = BudgetTrackerMaths.futureBudget(incomeComponents, growthRate)
    return jsonify(futureBudget.tolist())

# Calculates sustainable daily spending based on essential
@FlaskAPI.route("/sustainable-spending", methods=["POST"])
def sustainableSpending():
    data = request.json
    essentials = data.get("essentials")
    sustainableSpending = BudgetTrackerMaths.sustainableSpendingPerDay(essentials)
    return jsonify(sustainableSpending)

# Calculates total purchases over the last week based on prices and quantities
@FlaskAPI.route("/get-last-week-purchases", methods=["POST"])
def getLastWeekPurchases():
    data = request.json
    prices = data.get("prices")
    quantities = data.get("quantities")
    return jsonify(BudgetTrackerMaths.calculateBudgetOverAWeek(prices, quantities))

# Calculates total output using a Cobb-Douglas function
@FlaskAPI.route("/perform-cobb-douglas-function", methods=["POST"])
def performCobbDouglasFunction():
    data = request.json
    totalRevenue = data.get("totalRevenue")
    totalCost = data.get("totalCost")
    numberOfLabour = data.get("numberOfLabour")
    numberOfCapital = data.get("numberOfCapital")
    totalOutput = CostOfProductionMaths.cobbDouglasProductionFunction(totalRevenue, totalCost, numberOfCapital, numberOfLabour)
    return jsonify(totalOutput)

# AI-generated answer for a user question
@FlaskAPI.route("/ai-answer", methods=["POST"])
def aiAnswers():
    data = request.json
    userPrompt = data.get("userPrompt")
    aiReply = OllamaAI.aiAnswers(userPrompt)
    return jsonify(aiReply)

# Summarises a news article given its link
@FlaskAPI.route("/summarise-news-article", methods=["POST"])
def summariseNewsArticle():
    data = request.json
    link = data.get("link")
    aiReply = OllamaAI.summariseNewsArticle(link)
    return jsonify(aiReply)

# Fetches company information for a ticker symbol
@FlaskAPI.route("/company-information", methods=["POST"])
def companyInformation():
    data = request.json
    stock = data.get("tickerSymbol")
    stock = InvestmentData(stock)
    return jsonify(stock.getInfoOfCompany())

# Retrieves stock price data for a ticker and start date
@FlaskAPI.route("/stock-data", methods=["POST"])
def getStockPrice():
    data = request.json
    tickerSymbol = data.get("tickerSymbol")
    startDate = data.get("startDate")
    stock = InvestmentData(tickerSymbol)
    return jsonify(stock.getStockData(startDate))

# Retrieves current ask price for a stock ticker
@FlaskAPI.route("/stock-ask-price", methods=["POST"])
def getAskPrice():
    data = request.json
    tickerSymbol = data.get("tickerSymbol")
    stock = InvestmentData(tickerSymbol)
    return jsonify(stock.getAskPrice())

# Calculate SMA
@FlaskAPI.route("/calculate-SMA", methods=["POST"])
def getSMA():
    data = request.json
    tickerSymbol = data.get("tickerSymbol")
    window = int(data.get("window"))
    stock = InvestmentData(tickerSymbol)
    return jsonify(stock.getSMATrend(window))

if __name__ == '__main__':
    FlaskAPI.run(debug=True, port=5000)