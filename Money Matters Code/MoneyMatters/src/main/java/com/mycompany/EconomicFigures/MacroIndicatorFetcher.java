package com.mycompany.EconomicFigures;

public class MacroIndicatorFetcher {
    
    // Method that creates and returns a specific MacroIndicators fetcher
    public static MacroIndicators instantiateIndicator(String countryCode, String indicatorCode) {
        switch (indicatorCode) {
            // GDP (current US$)
            case "NY.GDP.MKTP.CD":
                return new GDPFetcher(countryCode, indicatorCode);

            // GDP per capita (current US$)
            case "NY.GDP.PCAP.CD":
                return new GDPPerCapitaFetcher(countryCode, indicatorCode);

            // Inflation, consumer prices (annual %)
            case "FP.CPI.TOTL.ZG":
                return new InflationFetcher(countryCode, indicatorCode);

            // Unemployment (% of total labour force)
            case "SL.UEM.TOTL.ZS":
                return new UnemploymentFetcher(countryCode, indicatorCode);
            
            // Real interest rate (%)
            case "FR.INR.RINR":    
                return new InterestRateFetcher(countryCode, indicatorCode);

            // Gini index (income inequality)
            case "SI.POV.GINI":
                return new GiniIndexFetcher(countryCode, indicatorCode);            
            
            // Government debt (% of GDP)
            case "GC.DOD.TOTL.GD.ZS":
                return new GovernmentDebtFetcher(countryCode, indicatorCode);
            
            // Net trade in goods and services (current US$)
            case "BN.GSR.GNFS.CD":
                return new NetTradeFetcher(countryCode, indicatorCode);
            
            // Trade (% of GDP)
            case "NE.TRD.GNFS.ZS":
                return new TradeFetcher(countryCode, indicatorCode);                
            
            // Tax revenue (current LCU)
            case "GC.TAX.TOTL.CN":
                return new TaxRevenueLCUFetcher(countryCode, indicatorCode);            
            
            // Tax revenue (% of GDP)
            case "GC.TAX.TOTL.GD.ZS":
                return new TaxRevenueGDPFetcher(countryCode, indicatorCode);            
            
            // Total reserves (current US$)
            case "FI.RES.TOTL.CD":
                return new TotalReservesFetcher(countryCode, indicatorCode);
            
            default:
                return null;
        }
    }
}