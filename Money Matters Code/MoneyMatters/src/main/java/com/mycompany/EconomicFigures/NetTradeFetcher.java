package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Net Trade values should be formatted when displayed
public class NetTradeFetcher extends MacroIndicators {

    public NetTradeFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".#"); // Format with one decimal place
        return df;
    }
}