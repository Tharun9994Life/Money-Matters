package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

//This class configures how GDP per capita values should be formatted when displayed
public class GDPPerCapitaFetcher extends MacroIndicators {

    public GDPPerCapitaFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(".00"); // Format numbers with two decimal places
        return df;
    }
}