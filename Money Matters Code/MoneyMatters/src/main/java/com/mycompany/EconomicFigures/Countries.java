package com.mycompany.EconomicFigures;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Countries extends WorldBankAPI {

    public Countries () {
        url = "https://api.worldbank.org/v2/country/?format=json&per_page=300";
        request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    }
    
    public LinkedHashMap<String, String> getCountryData() {
        LinkedHashMap<String, String> countryCodes = new LinkedHashMap<>();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode dataArray = root.get(1);

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode dataNode : dataArray) {
                    // Extract country name and country ID and store them in TreeMap
                    countryCodes.put(dataNode.get("name").asText(), dataNode.get("id").asText());
                }
            }
        } 

        catch (Exception ex) {
            System.out.println("Error");
        }

        return countryCodes;
    }
}