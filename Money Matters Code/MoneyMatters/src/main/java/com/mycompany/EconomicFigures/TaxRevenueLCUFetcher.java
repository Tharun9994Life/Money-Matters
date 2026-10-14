package com.mycompany.EconomicFigures;

import java.text.DecimalFormat;

// This class configures how Tax Revenue values (in local currency units) should be formatted when displayed
public class TaxRevenueLCUFetcher extends MacroIndicators {

    public TaxRevenueLCUFetcher(String countryCode, String indicatorCode) {
        super(countryCode, indicatorCode); // Call the parent constructor
    }
    
    @Override
    protected DecimalFormat configureFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false); // Disable commas or spaces in numbers
        return df;
    }
}