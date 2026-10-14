package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how interest rate values should be formatted when displayed
public class InterestRateFetcher extends MacroIndicators {
 
    public InterestRateFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##.##"); // Format numbers with up to two decimal places
        return df;
    }
}