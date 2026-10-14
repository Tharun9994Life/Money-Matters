package com.mycompany.PythonFlaskAPI;

import java.util.LinkedHashMap;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class PythonFlaskAPI {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Breakdown income into components (essential, leisure, savings)
    public static LinkedHashMap<String, Double> calculateIncomeComponents(double income) {
        try {
            LinkedHashMap<String, Double> payload = new LinkedHashMap<>();
            payload.put("income", income);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/calculate-Income-Components"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<LinkedHashMap<String, Double>>() {});
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    // Calculates total output using a Cobb-Douglas function
    public static String performCobbDouglasFunction(double totalRevenue, double totalCost, int numberOfLabour, int numberOfCapital) {
        try {
            LinkedHashMap<String, Double> payload = new LinkedHashMap<>();
            payload.put("totalRevenue", totalRevenue);
            payload.put("totalCost", totalCost);
            payload.put("numberOfLabour", (double) numberOfLabour);
            payload.put("numberOfCapital", (double) numberOfCapital);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/perform-cobb-douglas-function"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // AI-generated answer for a user question
    public static String aiAnswer(String userPrompt) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("userPrompt", userPrompt);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/ai-answer"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Summarises a news article given its link
    public static String summariseNewsArticle(String link) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("link", link);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/summarise-news-article"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Calculates future budget based on income components and inflation
    public static String futureBudget(LinkedHashMap<String, Double> incomeComponents, double growthRate) {
        try {
            LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
            payload.put("incomeComponents", incomeComponents);
            payload.put("growthRate", growthRate);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/future-budget"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Calculates sustainable daily spending based on essential
    public static Double sustainableSpending(Double essentials) {
        try {
            LinkedHashMap<String, Double> payload = new LinkedHashMap<>();
            payload.put("essentials", essentials);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/sustainable-spending"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Double.valueOf(response.body());
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Fetches company information for a ticker symbol
    public static LinkedHashMap<String, String> getCompanyInformation(String tickerSymbol) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("tickerSymbol", tickerSymbol);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/company-information"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<LinkedHashMap<String, String>>() {});
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Retrieves stock price data for a ticker and start date
    public static ArrayList<LinkedHashMap<String, String>> getStockPrice(String tickerSymbol, String startDate) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("tickerSymbol", tickerSymbol);
            payload.put("startDate", startDate);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/stock-data"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<ArrayList<LinkedHashMap<String, String>>>() {});
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Retrieves the current ask price of a stock
    public static Double getAskPrice(String tickerSymbol) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("tickerSymbol", tickerSymbol);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/stock-ask-price"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Double.valueOf(response.body());
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Calculates total purchases over the last week based on prices and quantities
    public static Double getLastWeekPurchases(ArrayList<ArrayList<Double>> priceQuantityPairs) {
        try {
            LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
            payload.put("prices", priceQuantityPairs.get(0));
            payload.put("quantities", priceQuantityPairs.get(1));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/get-last-week-purchases"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Double.valueOf(response.body());
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Calculates SMA
    public static LinkedHashMap<String, Object> calculateSMA(String tickerSymbol, int window) {
        try {
            LinkedHashMap<String, String> payload = new LinkedHashMap<>();
            payload.put("tickerSymbol", tickerSymbol);
            payload.put("window", Integer.toString(window));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/calculate-SMA"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<LinkedHashMap<String, Object>>() {});
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}