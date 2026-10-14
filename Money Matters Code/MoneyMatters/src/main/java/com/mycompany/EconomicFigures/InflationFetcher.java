package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how inflation values should be formatted when displayed
public class InflationFetcher extends MacroIndicators {

    public InflationFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".0"); // Format numbers with one decimal place
        return df;
    }
}