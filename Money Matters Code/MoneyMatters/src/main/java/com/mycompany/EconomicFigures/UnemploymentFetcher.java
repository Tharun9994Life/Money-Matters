package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Unemployment values should be formatted when displayed
public class UnemploymentFetcher extends MacroIndicators {

    public UnemploymentFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0.#"); // Format numbers with one decimal place
        return df;
    }
}