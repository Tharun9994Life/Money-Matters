package com.mycompany.Controllers;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.EconomicFigures.ExchangeRateAPI;

@RestController
public class ExchangeRateController {

    // Display the exchange rate page with available currencies and dates
    @GetMapping("exchange-rate-economic")
    public ModelAndView showExchangeRate() {
        ModelAndView mv = new ModelAndView("economicfigures/exchange-rate-economic");

        // Provide list of available currency codes and dates
        mv.addObject("avaliableCurrencies", ExchangeRateAPI.addCurrencyCodesPairs());
        mv.addObject("avaliableDates", ExchangeRateAPI.addDates());

        return mv;
    }

    // Fetch exchange rate between two currencies
    @PostMapping("exchange-rate-economic")
    public ModelAndView getExchangeRate(@RequestParam String baseCurrency, 
                    @RequestParam String wantedCurrency,
                    @RequestParam LocalDate date) {
        ModelAndView mv = new ModelAndView("economicfigures/exchange-rate-economic");

        // Date is not in the future (use yesterday if so)
        if (date.isAfter(LocalDate.now().minusDays(1))) {
            date = LocalDate.now().minusDays(1);
        }
        
        // If base and target currencies are the same; rate is 1
        if (baseCurrency.equals(wantedCurrency)) {
            mv.addObject("convertedCurrency", 1);
        } 
        
        else {
            // Fetch conversion rate
            mv.addObject("convertedCurrency", ExchangeRateAPI.compareTwoCurrencies(baseCurrency, wantedCurrency, date));
        }

        mv.addObject("seriesOfCurrencies", ExchangeRateAPI.seriesOfCurrencies(baseCurrency, wantedCurrency, date));

        mv.addObject("date", date);
        mv.addObject("baseCurrency", baseCurrency);
        mv.addObject("wantedCurrency", wantedCurrency);

        mv.addObject("avaliableCurrencies", ExchangeRateAPI.addCurrencyCodesPairs());
        mv.addObject("avaliableDates", ExchangeRateAPI.addDates());

        mv.addObject("selectedBaseCurrency", baseCurrency);
        mv.addObject("selectedWantedCurrency", wantedCurrency);

        return mv;
    }
}