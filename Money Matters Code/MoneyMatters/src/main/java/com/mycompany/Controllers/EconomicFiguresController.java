package com.mycompany.Controllers;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Algorithms.BinarySearch;
import com.mycompany.EconomicFigures.Countries;
import com.mycompany.EconomicFigures.MacroIndicatorFetcher;
import com.mycompany.EconomicFigures.MacroIndicators;

@RestController
public class EconomicFiguresController {

    @GetMapping("figures-economic")
    public ModelAndView showEconomicFigures() {
        ModelAndView mv = new ModelAndView("economicfigures/figures-economic");

        // Instantiate the Countries object to get all country data
        Countries countries = new Countries();
        LinkedHashMap<String, String> countryCodes = countries.getCountryData();

        mv.addObject("countryCodes", countryCodes);

        return mv;
    }

    @PostMapping("figures-economic")
    public ModelAndView getEconomicFigures(@RequestParam String countryCode, 
                                            @RequestParam String indicatorCode) {
        
        ModelAndView mv = new ModelAndView("economicfigures/figures-economic");

        Countries countriesObj = new Countries();
        LinkedHashMap<String, String> countryCodes = countriesObj.getCountryData();
        mv.addObject("countryCodes", countryCodes);

        // Convert TreeMap values and keys to ArrayLists for index access
        ArrayList<String> codes = new ArrayList<>(countryCodes.values());
        ArrayList<String> countries = new ArrayList<>(countryCodes.keySet());

        // Find the selected country and add it to the model
        int indexOfCountryCode = BinarySearch.binarySearchObject(codes, 0, codes.size(), countryCode);
        mv.addObject("country", countries.get(indexOfCountryCode));
        mv.addObject("selectedCountry", countries.get(indexOfCountryCode));

        // Fetch macroeconomic data for the selected country and indicator
        MacroIndicators macroIndicators = MacroIndicatorFetcher.instantiateIndicator(countryCode, indicatorCode);
        HashMap<Year, BigDecimal> yearDataPairs = macroIndicators.getData();

        mv.addObject("yearDataPairs", yearDataPairs);
        mv.addObject("indicatorCode", indicatorCode);

        switch (indicatorCode) {
            case "NY.GDP.MKTP.CD":
                mv.addObject("selectedIndicator", "GDP");
                break;
            case "NY.GDP.PCAP.CD":
                mv.addObject("selectedIndicator", "GDP Per Capita");
                break;
            case "FP.CPI.TOTL.ZG":
                mv.addObject("selectedIndicator", "Inflation (CPI)");
                break;
            case "SL.UEM.TOTL.ZS":
                mv.addObject("selectedIndicator", "Real Interest Rate");
                break;
            case "FR.INR.RINR":
                mv.addObject("selectedIndicator", "Total Unemployment");
                break;
            case "SI.POV.GINI":
                mv.addObject("selectedIndicator", "Gini Index");
                break;
            case "GC.DOD.TOTL.GD.ZS":
                mv.addObject("selectedIndicator", "Total Central Government Debt");
                break;
            case "BN.GSR.GNFS.CD":
                mv.addObject("selectedIndicator", "Net Trade In Goods & Services");
                break;
            case "NE.TRD.GNFS.ZS":
                mv.addObject("selectedIndicator", "Trade (% of GDP)");
                break;
            case "GC.TAX.TOTL.CN":
                mv.addObject("selectedIndicator", "Tax revenue (current LCU)");
                break;
            case "GC.TAX.TOTL.GD.ZS":
                mv.addObject("selectedIndicator", "Tax revenue (% of GDP)");
                break;
            case "FI.RES.TOTL.CD":
                mv.addObject("selectedIndicator", "Total reserves - includes gold");
                break;
            default:
                break;
        }

        return mv;
    }
}