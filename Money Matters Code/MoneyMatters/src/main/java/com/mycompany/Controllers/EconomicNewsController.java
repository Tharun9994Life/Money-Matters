package com.mycompany.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Algorithms.MergeSort;
import com.mycompany.Logic.WebScraperLogic;
import com.mycompany.PythonFlaskAPI.APIQueue;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Records.EconomicNewsRecord;

@RestController
public class EconomicNewsController {

    private static final APIQueue taskManager = new APIQueue(3);

    // Store scraped economic news articles
    private final ArrayList<EconomicNewsRecord> economicNews = WebScraperLogic.scrapeNews();

    @GetMapping("news")
    public ModelAndView showNews() {
        ModelAndView mv = new ModelAndView("economicnews/news");

        // Sort articles by date (latest first)
        MergeSort.mergeSortEconomicNewsByDate(economicNews);
        Collections.reverse(economicNews);

        mv.addObject("economicNews", economicNews);

        // Default selected sources = all sources
        // 0 - BBCRSSFeed
        // 1 - TheTelegraphRSSFeed
        // 2 - TheEconomistRSSFeed
        // 3 - TheIndependentRSSFeed
        // 4 - CNBCRSSFeed
        ArrayList<Integer> selectedSources = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        mv.addObject("selectedSources", selectedSources);

        mv.addObject("selectedSort", "date (latest)");
        return mv;
    }

    // Handle filtering and AI summarisation requests from the news page
    @PostMapping("news")
    public Object summariseBBCNewsArticle(
        @RequestParam String formName,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) String link, 
        @RequestParam(required = false) ArrayList<Integer> newsSource) {

        // Filter and sort news articles
        if (formName.equals("filterNews")) {
            ModelAndView mv = new ModelAndView("economicnews/news");
            ArrayList<EconomicNewsRecord> reducedEconomicNews = new ArrayList<>();

            // Group news by source
            ArrayList<ArrayList<EconomicNewsRecord>> economicNewsBySource = WebScraperLogic.sortEconomicsNewsBySource(economicNews);

            if (newsSource == null) {
                mv.addObject("economicNews", reducedEconomicNews);
                mv.addObject("selectedSources", new ArrayList<>());
                mv.addObject("selectedSort", sort);
                return mv;
            }

            // Collect news from the selected sources
            for (int i = 0; i < newsSource.size(); i++) {
                reducedEconomicNews.addAll(economicNewsBySource.get(newsSource.get(i)));
            }

            mv.addObject("selectedSources", newsSource);

            // Apply sorting option selected by user
            switch (sort) {
                case "date (earliest)":
                    MergeSort.mergeSortEconomicNewsByDate(reducedEconomicNews);
                    break;
                case "date (latest)":
                    MergeSort.mergeSortEconomicNewsByDate(reducedEconomicNews);
                    Collections.reverse(reducedEconomicNews);
                    break;
                case "title (a-z)":
                    MergeSort.mergeSortEconomicNewsByTitle(reducedEconomicNews);
                    break;
                case "title (z-a)":
                    MergeSort.mergeSortEconomicNewsByTitle(reducedEconomicNews);
                    Collections.reverse(reducedEconomicNews);
                    break;
                default:
                    break;
            }

            mv.addObject("selectedSort", sort);
            mv.addObject("economicNews", reducedEconomicNews);
            
            return mv;
        }
        
        // Summarise news articles using Python Flask API
        else if (formName.equals("summarise")) {
            try {
                Future<Map<String, String>> future = taskManager.submitTask(() -> {
                    return Map.of("aiReply", PythonFlaskAPI.summariseNewsArticle(link));
                });

                // Wait for the AI summarisation to complete
                Map<String, String> result = future.get();
                return result;
            }

            catch (Exception ex) {

            }

        }

        return null;
    }
}