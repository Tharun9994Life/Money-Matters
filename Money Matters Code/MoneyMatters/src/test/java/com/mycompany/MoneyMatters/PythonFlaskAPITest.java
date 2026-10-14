package com.mycompany.MoneyMatters;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PythonFlaskAPITest {

    private ArrayList<LinkedHashMap<String, ?>> getPayloads() {
        final Random randomNumberGenerator = new Random();
        ArrayList<LinkedHashMap<String, ?>> payloads = new ArrayList<>();

        LinkedHashMap<String, Double> payload1 = new LinkedHashMap<>();            
        payload1.put("income", randomNumberGenerator.nextDouble(10, 1000000));

        LinkedHashMap<String, Double> payload2 = new LinkedHashMap<>();            
        payload2.put("totalRevenue", randomNumberGenerator.nextDouble(10, 1000000));
        payload2.put("totalCost", randomNumberGenerator.nextDouble(10, 1000000));
        payload2.put("numberOfLabour", randomNumberGenerator.nextDouble(10, 1000000));
        payload2.put("numberOfCapital", randomNumberGenerator.nextDouble(10, 1000000));

        LinkedHashMap<String, String> payload3 = new LinkedHashMap<>();
        payload3.put("userPrompt", "Define GDP");

        LinkedHashMap<String, String> payload4 = new LinkedHashMap<>();
        payload4.put("link", "https://www.independent.co.uk/news/business/business-rates-warning-supermarkets-price-b2852194.html");

        LinkedHashMap<String, Object> payload5 = new LinkedHashMap<>();
        LinkedHashMap<String, Double> incomeComponents = new LinkedHashMap<>();
        incomeComponents.put("Essentials", randomNumberGenerator.nextDouble(10, 1000000));
        incomeComponents.put("Leisure", randomNumberGenerator.nextDouble(10, 1000000));
        incomeComponents.put("Savings", randomNumberGenerator.nextDouble(10, 1000000));
        payload5.put("incomeComponents", incomeComponents);
        payload5.put("growthRate", randomNumberGenerator.nextDouble(0, 10));

        LinkedHashMap<String, Double> payload6 = new LinkedHashMap<>();
        payload6.put("essentials", randomNumberGenerator.nextDouble(10, 1000000));

        LinkedHashMap<String, String> payload7 = new LinkedHashMap<>();
        payload7.put("tickerSymbol", "AAPL");

        LinkedHashMap<String, String> payload8 = new LinkedHashMap<>();
        payload8.put("tickerSymbol", "AAPL");
        payload8.put("startDate", LocalDate.now().toString());

        LinkedHashMap<String, String> payload9 = new LinkedHashMap<>();
        payload9.put("tickerSymbol", "AAPL");

        LinkedHashMap<String, Object> payload10 = new LinkedHashMap<>();
        payload10.put("prices", new ArrayList<Double>());
        payload10.put("quantities", new ArrayList<Integer>());

        LinkedHashMap<String, Object> payload11 = new LinkedHashMap<>();
        payload11.put("tickerSymbol", "AAPL");
        payload11.put("window", randomNumberGenerator.nextInt(10, 250));

        payloads.add(payload1);
        payloads.add(payload2);
        payloads.add(payload3);
        payloads.add(payload4);
        payloads.add(payload5);
        payloads.add(payload6);
        payloads.add(payload7);
        payloads.add(payload8);
        payloads.add(payload9);
        payloads.add(payload10);
        payloads.add(payload11);
        return payloads;
    }

    @Test
    public void testPythonFlaskAPI() throws Exception {
        final HttpClient client = HttpClient.newHttpClient();
        final ObjectMapper mapper = new ObjectMapper();
        final String baseURL = "http://localhost:5000/";
        final String[] endpoints = {"calculate-Income-Components", "perform-cobb-douglas-function", "ai-answer", "summarise-news-article", "future-budget", "sustainable-spending", "company-information", "stock-data", "stock-ask-price", "get-last-week-purchases", "calculate-SMA"};
        final ArrayList<LinkedHashMap<String, ?>> payloads = getPayloads();

        for (int i = 0; i < endpoints.length; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + endpoints[i]))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payloads.get(i))))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            System.out.println("Endpoint: " + endpoints[i]);
            System.out.println("Sample Data: " + payloads.get(i).toString());
            System.out.println("Response: " + mapper.readValue(response.body(), Object.class));
            System.out.println("");
        }
    }
}