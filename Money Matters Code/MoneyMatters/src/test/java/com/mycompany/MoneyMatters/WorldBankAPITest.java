package com.mycompany.MoneyMatters;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.EconomicFigures.Countries;

public class WorldBankAPITest {
    public String randomCountryCode() {
        final Countries countries = new Countries();
        final Random randomNumberGenerator = new Random();
        LinkedHashMap<String, String> countryCodes = countries.getCountryData();
        ArrayList<String> country = new ArrayList<>(countryCodes.keySet());

        return countryCodes.get(country.get(randomNumberGenerator.nextInt(country.size() + 1)));
    }

    public String randomIndicatorCode() {
        final Random randomNumberGenerator = new Random();
        ArrayList<String> indicatorCodes = new ArrayList<>();

        // GDP (current US$)
        indicatorCodes.add("NY.GDP.MKTP.CD");
        
        // GDP per capita (current US$)
        indicatorCodes.add("NY.GDP.PCAP.CD");

        // Inflation, consumer prices (annual %)
        indicatorCodes.add("FP.CPI.TOTL.ZG");
            
        // Unemployment (% of total labour force)
        indicatorCodes.add("SL.UEM.TOTL.ZS");
            
        // Real interest rate (%)
        indicatorCodes.add("FR.INR.RINR");    
            
        // Gini index (income inequality)
        indicatorCodes.add("SI.POV.GINI");

        // Government debt (% of GDP)
        indicatorCodes.add("GC.DOD.TOTL.GD.ZS");

        // Net trade in goods and services (current US$)
        indicatorCodes.add("BN.GSR.GNFS.CD");
            
        // Trade (% of GDP)
        indicatorCodes.add("NE.TRD.GNFS.ZS");
        
        // Tax revenue (current LCU)
        indicatorCodes.add("GC.TAX.TOTL.CN");
            
        // Tax revenue (% of GDP)
        indicatorCodes.add("GC.TAX.TOTL.ZS");
            
        // Total reserves (current US$)
        indicatorCodes.add("FI.RES.TOTL.CD");

        return indicatorCodes.get(randomNumberGenerator.nextInt(indicatorCodes.size()));
    }

    @RepeatedTest(10) 
    public void testWorldBankAPI(RepetitionInfo repetitionInfo) throws Exception {
        final HttpClient client = HttpClient.newHttpClient();  
        final String countryCode = randomCountryCode();
        final String indicatorCode = randomIndicatorCode();
        final String url = "https://api.worldbank.org/v2/countries/" + countryCode + "/indicators/" + indicatorCode + "?format=json&per_page=1";
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        final ObjectMapper mapper = new ObjectMapper();

        System.out.println("");
        System.out.println("Test " + repetitionInfo.getCurrentRepetition() + ":");
        System.out.println("Country Code: " + countryCode);
        System.out.println("Indicator Code: " + indicatorCode); 
        System.out.println("URL: " + url);
        
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());            
        JsonNode root = mapper.readTree(response.body());        
        JsonNode dataArray = root.get(1);
        JsonNode dataNode = dataArray.get(0);

        assertNotNull(dataNode.get("value"));

        System.out.println("Value: " + dataNode.get("value"));
    }
}