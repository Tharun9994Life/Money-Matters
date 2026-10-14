package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

//This class configures how Gini Index values should be formatted when displayed
public class GiniIndexFetcher extends MacroIndicators {

    public GiniIndexFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##.#0"); // Format numbers with one decimal place (e.g., 35.70)
        return df;
    }
}