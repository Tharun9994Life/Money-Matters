//https://frankfurter.dev/

package com.mycompany.EconomicFigures;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExchangeRateAPI {

    public static TreeMap<String, String> addCurrencyCodesPairs () {
        TreeMap<String, String> currencyCodesPairs = new TreeMap<>();
        currencyCodesPairs.put("Australian Dollar", "AUD");
        currencyCodesPairs.put("Bulgarian Lev", "BGN");
        currencyCodesPairs.put("Brazilian Real", "BRL");
        currencyCodesPairs.put("Canadian Dollar", "CAD");
        currencyCodesPairs.put("Swiss Franc", "CHF");
        currencyCodesPairs.put("Chinese Renminbi Yuan", "CNY");
        currencyCodesPairs.put("Czech Koruna", "CZK");
        currencyCodesPairs.put("Danish Krone", "DKK");
        currencyCodesPairs.put("Euro", "EUR");
        currencyCodesPairs.put("British Pound", "GBP");
        currencyCodesPairs.put("Hong Kong Dollar", "HKD");
        currencyCodesPairs.put("Hungarian Forint", "HUF");
        currencyCodesPairs.put("Indonesian Rupiah", "IDR");
        currencyCodesPairs.put("Israeli New Sheqel", "ILS");
        currencyCodesPairs.put("Indian Rupee", "INR");
        currencyCodesPairs.put("Japanese Yen", "JPY");
        currencyCodesPairs.put("South Korean Won", "KRW");
        currencyCodesPairs.put("Mexican Peso", "MXN");
        currencyCodesPairs.put("Malaysian Ringgit", "MYR");
        currencyCodesPairs.put("Norwegian Krone", "NOK");
        currencyCodesPairs.put("New Zealand Dollar", "NZD");
        currencyCodesPairs.put("Philippine Peso", "PHP");
        currencyCodesPairs.put("Polish Złoty", "PLN");
        currencyCodesPairs.put("Romanian Leu", "RON");
        currencyCodesPairs.put("Swedish Krona", "SEK");
        currencyCodesPairs.put("Singapore Dollar", "SGD");
        currencyCodesPairs.put("Thai Baht", "THB");
        currencyCodesPairs.put("Turkish Lira", "TRY");
        currencyCodesPairs.put("United States Dollar", "USD");
        currencyCodesPairs.put("South African Rand", "ZAR");
        return currencyCodesPairs;
    }

    public static LinkedHashMap<String, LocalDate> addDates() {
        LinkedHashMap<String, LocalDate> dates = new LinkedHashMap<>();
        LocalDate desiredDate = LocalDate.now().minusDays(1);
        dates.put("Now", desiredDate);
        dates.put("A Day Ago", desiredDate.minusDays(1));
        dates.put("A Week Ago", desiredDate.minusWeeks(1));
        dates.put("A Month Ago", desiredDate.minusMonths(1));
        dates.put("6 Months Ago", desiredDate.minusMonths(6));
        dates.put("1 Year Ago", desiredDate.minusYears(1));
        dates.put("5 Years Ago", desiredDate.minusYears(5));
        dates.put("10 Years Ago", desiredDate.minusYears(10));
        return dates;
    }
    
    // Fetches the exchange rate for a single day between two currencies.
    public static String compareTwoCurrencies(String baseCurrency, String wantedCurrency, LocalDate date) {
        String convertedCurrency = null;

        StringBuilder url = new StringBuilder("https://api.frankfurter.app/");
        HttpClient client = HttpClient.newHttpClient();
        
        // Build API request URL
        url.append(date);
        url.append("?base=").append(baseCurrency);
        url.append("&symbols=").append(wantedCurrency);
        
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            convertedCurrency = root.get("rates").get(wantedCurrency).asText();
        }

        catch (Exception ex) {
            System.out.println("Error");
        }

        return convertedCurrency;
    }

    // Fetches a series of exchange rates between two currencies across multiple dates.
    public static LinkedHashMap<String, Double> seriesOfCurrencies(String baseCurrency, String wantedCurrency, LocalDate date) {
        StringBuilder url = new StringBuilder("https://api.frankfurter.app/");
        HttpClient client = HttpClient.newHttpClient();
        LinkedHashMap<String, Double> dataSeries = new LinkedHashMap<>();
        
        url.append(date);
        url.append("..");
        url.append("?base=").append(baseCurrency);
        url.append("&symbols=").append(wantedCurrency);
        System.out.println(url.toString());
        
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode ratesNode = root.get("rates");
            
            Iterator<String> dates = ratesNode.fieldNames();
            while (dates.hasNext()) {
                String nextDate = dates.next();
                JsonNode rateNode = ratesNode.get(nextDate).get(wantedCurrency);
                dataSeries.put(nextDate, rateNode.asDouble());
            }
        }

        catch (Exception ex) {
            System.out.println("Error");
        }

        return dataSeries;
    }
}