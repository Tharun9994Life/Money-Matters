package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

//This class configures how GDP values should be formatted when displayed
public class GDPFetcher extends MacroIndicators {

    public GDPFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }

    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false); // Disable commas or spaces in numbers
        return df;
    }
}