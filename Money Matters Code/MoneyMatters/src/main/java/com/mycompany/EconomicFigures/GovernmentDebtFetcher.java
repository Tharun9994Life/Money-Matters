package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

//This class configures how government debt values should be formatted when displayed
public class GovernmentDebtFetcher extends MacroIndicators {

    public GovernmentDebtFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".#0"); // Format numbers with two decimal place
        return df;
    }
}