package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Trade values should be formatted when displayed
public class TradeFetcher extends MacroIndicators {

    public TradeFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".#"); // Format numbers with two decimal place
        return df;
    }
}