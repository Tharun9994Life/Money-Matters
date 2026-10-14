# https://github.com/yahoo-finance/yahoo-finance

import yfinance as yf
from datetime import datetime, date, timedelta
import numpy as np

class InvestmentData:
    def __init__(self, tickerSymbol):
        self.tickerSymbol = tickerSymbol
        self.ticker = yf.Ticker(self.tickerSymbol)
        self.info = self.ticker.info
        
    def getInfoOfCompany(self):
        # Extract company information
        fields = ["symbol", "shortName", "sector", "industry", "fullTimeEmployees", "country", "website"]
        companyInfo = {}
        
        for field in fields:
            companyInfo[field] = self.ticker.info.get(field)
            
        return companyInfo
    
    def getStockData(self, startDate):
        currentStockData = []
        startDate = datetime.strptime(startDate, "%Y-%m-%d").date()
        endDate = date.today() - timedelta(days=1)
        
        # Fetch historical stock data
        data = self.ticker.history(start=startDate, end=endDate)
        
        for i in range(0, len(data)):
            currentStockDataInstance = {}
            currentStockDataInstance["Date"] = data.index[i].strftime("%d/%m/%y")
            currentStockDataInstance["Open"] = round(float(data["Open"].iloc[i]), 2)
            currentStockDataInstance["High"] = round(float(data["High"].iloc[i]), 2)
            currentStockDataInstance["Low"] = round(float(data["Low"].iloc[i]), 2)
            currentStockDataInstance["Close"] = round(float(data["Close"].iloc[i]), 2)
            currentStockDataInstance["Volume"] = int(data["Volume"].iloc[i])
            
            # Add the day's data to the main list
            currentStockData.append(currentStockDataInstance)
        
        # Return a list of dictionaries with historical stock data
        return currentStockData
    
    # Retrieve the current ask price
    def getAskPrice(self):
        return self.info.get("ask")
   
    def getSMATrend(self, window):
        end_date = date.today()
        start_date = end_date - timedelta(days=window * 3)
        data = self.ticker.history(start=start_date, end=end_date)

        if data.empty or len(data) < window:
            return {"error": "Not enough data"}

        # Calculate SMA
        data["SMA"] = data["Close"].rolling(window=window).mean().dropna()
        data = data.dropna(subset=["SMA"])
        
        # Use Linear Regression to calculate the gradient of the SMA line
        y = data["SMA"].to_numpy()
        x = np.arange(len(y))

        coefficients = np.polyfit(x, y, 1)
        gradient = coefficients[0]

        # Calculate the SMA trend based on the gradient
        if gradient > 0.25:
            trend = "Increasing"
        elif gradient < 0.25:
            trend = "Decreasing"
        else:
            trend = "Stable"

        return {
            "trend": trend,
            "gradient": float(gradient)
        }