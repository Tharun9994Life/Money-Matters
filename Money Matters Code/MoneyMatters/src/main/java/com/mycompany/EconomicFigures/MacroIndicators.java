package com.mycompany.EconomicFigures;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MacroIndicators extends WorldBankAPI {
    protected Year year;
    protected BigDecimal value;
    protected String indicatorCode;
    protected String countryCode;

    // Sets up the HTTP request
    public MacroIndicators(String countryCode, String indicatorCode) {
        this.countryCode = countryCode;
        this.indicatorCode = indicatorCode;

        url = "https://api.worldbank.org/v2/countries/" + this.countryCode + "/indicators/" + this.indicatorCode + "?format=json&per_page=1000";
        request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    }
    
    // Each subclass defines how to format the numeric value for display
    protected abstract DecimalFormat configureFormat();

    // Fetches data from the World Bank API
    public HashMap<Year, BigDecimal> getData() {
        HashMap<Year, BigDecimal> yearDataPairs = new HashMap<>();
        
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode dataArray = root.get(1);
            DecimalFormat df = configureFormat();

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode dataNode : dataArray) {
                    boolean skip = false;
                    year = Year.of(dataNode.get("date").asInt());
                    
                    if (dataNode.get("value").isNull()) {
                        skip = true;
                    }

                    if (!skip) {
                        value = new BigDecimal(df.format(new BigDecimal(dataNode.get("value").asText())));
                        yearDataPairs.put(year, value);
                    }
                }
            }
        } 
        catch (Exception ex) {
            System.out.println("Error fetching macro indicator data");
        }

        return yearDataPairs;
    }
}