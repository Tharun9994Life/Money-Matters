package com.mycompany.EconomicFigures;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

// Abstract class for World Bank API requests.
public abstract class WorldBankAPI {
    protected String url = "https://api.worldbank.org/v2/";
    protected final HttpClient client = HttpClient.newHttpClient();

    // Subclasses override this with more specific endpoints
    protected HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();  
}