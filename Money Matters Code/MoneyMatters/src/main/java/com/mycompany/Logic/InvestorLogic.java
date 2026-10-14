package com.mycompany.Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mycompany.Algorithms.FileHandlingJSON;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Records.CompanyRecord;

public class InvestorLogic {

    private static final File tickerInfo = new File("tickerInfo.json");
    private static final FileHandlingJSON JSONHandling = new FileHandlingJSON(); 

    // Retrieves all company names and their ticker symbols from JSON
    public static LinkedHashMap<String, String> getAllTickerInfo() {
        ArrayList<LinkedHashMap<String, String>> allTickerInfoAsJSON = JSONHandling.readFileList(tickerInfo, new TypeReference<List<LinkedHashMap<String, String>>>() {});
        LinkedHashMap<String, String> allTickerInfo = new LinkedHashMap<>();

        for (LinkedHashMap<String, String> entry : allTickerInfoAsJSON) {
            allTickerInfo.put(entry.get("companyName"), entry.get("tickerSymbol"));
        }

        return allTickerInfo;
    }

    // Retrieves all ticker symbols and their company names (reversed map)
    public static LinkedHashMap<String, String> getAllTickerInfoReversed() {
        ArrayList<LinkedHashMap<String, String>> allTickerInfoAsJSON =
            JSONHandling.readFileList(tickerInfo, new TypeReference<List<LinkedHashMap<String, String>>>() {});

        LinkedHashMap<String, String> allTickerInfoReversed = new LinkedHashMap<>();

        for (LinkedHashMap<String, String> entry : allTickerInfoAsJSON) {
            allTickerInfoReversed.put(entry.get("tickerSymbol"), entry.get("companyName"));
        }

        return allTickerInfoReversed;
    }

    // Fetches company information
    public static CompanyRecord getCompanyInfo(String tickerSymbol) {
        LinkedHashMap<String, String> companyData = PythonFlaskAPI.getCompanyInformation(tickerSymbol);
        String fullTimeEmployees;

        try {
            fullTimeEmployees = companyData.get("fullTimeEmployees");
        } 
        
        catch (Exception ex) {
            fullTimeEmployees = "Unknown";
        }

        return new CompanyRecord(
            companyData.get("symbol"),
            companyData.get("shortName"),
            companyData.get("sector"),
            companyData.get("industry"),
            fullTimeEmployees,
            companyData.get("country"),
            companyData.get("website")
        ); 
    }

    // Retrieves stock price data for a ticker and splits it into separate components
    public static ArrayList<ArrayList<?>> getStockPriceDataComponents(String tickerSymbol, String timeSpan) {
        ArrayList<LinkedHashMap<String, String>> allStockPriceData = PythonFlaskAPI.getStockPrice(tickerSymbol, timeSpan);

        ArrayList<ArrayList<?>> stockPriceDataComponents = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Double> opens = new ArrayList<>();
        ArrayList<Double> highs = new ArrayList<>();
        ArrayList<Double> lows = new ArrayList<>();
        ArrayList<Double> closes = new ArrayList<>();
        ArrayList<Long> volumes = new ArrayList<>();

        // Iterates through all stock data and appends lists
        for (LinkedHashMap<String, String> dataPoint : allStockPriceData) {
            dates.add(dataPoint.get("Date"));
            opens.add(Double.valueOf(dataPoint.get("Open")));
            highs.add(Double.valueOf(dataPoint.get("High")));
            lows.add(Double.valueOf(dataPoint.get("Low")));
            closes.add(Double.valueOf(dataPoint.get("Close")));
            volumes.add(Long.valueOf(dataPoint.get("Volume")));
        }

        stockPriceDataComponents.add(dates);
        stockPriceDataComponents.add(opens);
        stockPriceDataComponents.add(highs);
        stockPriceDataComponents.add(lows);
        stockPriceDataComponents.add(closes);
        stockPriceDataComponents.add(volumes);

        return stockPriceDataComponents;
    }
}