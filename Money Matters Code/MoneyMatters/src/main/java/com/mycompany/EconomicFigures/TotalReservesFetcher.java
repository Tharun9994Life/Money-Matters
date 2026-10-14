package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Total Reserves values should be formatted when displayed
public class TotalReservesFetcher extends MacroIndicators {

    public TotalReservesFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".00"); // Format numbers with two decimal places
        return df;
    }
}