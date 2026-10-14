package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Tax Revenue (as a % of GDP) values should be formatted when displayed
public class TaxRevenueGDPFetcher extends MacroIndicators {

    public TaxRevenueGDPFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".00"); // Format with two decimal place
        return df;
    }
}