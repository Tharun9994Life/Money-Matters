package com.mycompany.Logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.mycompany.Records.EconomicNewsRecord;

public class WebScraperLogic {

    // RSS feed URLs for different news sources
    private final static String BBCRSSFeed = "https://feeds.bbci.co.uk/news/business/economy/rss.xml";
    private final static String TheTelegraphRSSFeed = "https://www.telegraph.co.uk/business/rss.xml";
    private final static String TheEconomistRSSFeed = "https://www.economist.com/finance-and-economics/rss.xml";
    private final static String TheIndependentRSSFeed = "https://www.independent.co.uk/news/business/rss";
    private final static String CNBCRSSFeed = "https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=10000664";

    // Scrapes news from RSS feeds and returns a list of EconomicNewsRecord
    public static ArrayList<EconomicNewsRecord> scrapeNews() {
        ArrayList<EconomicNewsRecord> economicNews = new ArrayList<>();
        LinkedHashMap<String, String> RSSFeeds = new LinkedHashMap<>();
        RSSFeeds.put("BBC News", BBCRSSFeed);
        RSSFeeds.put("The Telegraph", TheTelegraphRSSFeed);
        RSSFeeds.put("The Economist", TheEconomistRSSFeed);
        RSSFeeds.put("The Independent", TheIndependentRSSFeed);
        RSSFeeds.put("CNBC", CNBCRSSFeed);

        ArrayList<String> RSSFeedsSource = new ArrayList<>(RSSFeeds.keySet());
        ArrayList<String> RSSFeedsLinks = new ArrayList<>(RSSFeeds.values());

        int economicNewsID = 1;

        // Iterate over all RSS feed URLs
        for (int i = 0; i < RSSFeedsLinks.size(); i++) {
            String URL = RSSFeedsLinks.get(i);

            try {
                InputStream inputLink = new URL(URL).openStream();
                Document doc = Jsoup.parse(inputLink, "UTF-8", "", Parser.xmlParser());
                Elements headlines = doc.select("item");    // Each <item> is a news article

                // Limit to max 50 items per feed
                int maxSize = Math.min(headlines.size(), 50);

                for (int j = 0; j < maxSize; j++) {
                    Element headline = headlines.get(j);
                    String title = headline.select("title").text();
                    String description = headline.select("description").text();

                    // Parse publication date and format it
                    LocalDate pubDate = ZonedDateTime.parse(headline.select("pubDate").text(), DateTimeFormatter.RFC_1123_DATE_TIME).toLocalDate();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String pubDateFormatted = pubDate.format(df);

                    String link = headline.select("link").text();
                    String image = null;
                    String source = RSSFeedsSource.get(i);

                    // Extract Image based on source
                    switch (URL) {
                        case BBCRSSFeed:
                            image = headline.select("media\\:thumbnail").attr("url");
                            break;
                        case TheTelegraphRSSFeed:
                            image = headline.select("enclosure").attr("url");
                            break;
                        case TheIndependentRSSFeed:
                            image = headline.select("media\\:content").attr("url");
                            break;
                        default:
                            break;
                    }

                    // Add the news item to the list
                    economicNews.add(new EconomicNewsRecord(economicNewsID, title, description, pubDateFormatted, link, image, source));
                    economicNewsID++;
                }
            } 

            catch (IOException e) {
                System.out.println("An error occurred in scraping the news from " + URL);
            }
        }

        return economicNews;
    }

    // Sorts a list of EconomicNewsRecord by their news source
    public static ArrayList<ArrayList<EconomicNewsRecord>> sortEconomicsNewsBySource(ArrayList<EconomicNewsRecord> economicNews) {
        ArrayList<ArrayList<EconomicNewsRecord>> economicNewsBySource = new ArrayList<>();
        ArrayList<EconomicNewsRecord> BBCNews = new ArrayList<>();
        ArrayList<EconomicNewsRecord> TheTelegraph = new ArrayList<>();
        ArrayList<EconomicNewsRecord> TheEconomist = new ArrayList<>();
        ArrayList<EconomicNewsRecord> TheIndependent = new ArrayList<>();
        ArrayList<EconomicNewsRecord> CNBC = new ArrayList<>();

        // Group news items by source
        for (EconomicNewsRecord record : economicNews) {
            switch (record.source()) {
                case "BBC News": 
                    BBCNews.add(record);
                    break;
                case "The Telegraph": 
                    TheTelegraph.add(record);
                    break;
                case "The Economist":
                    TheEconomist.add(record);
                    break;
                case "The Independent": 
                    TheIndependent.add(record);
                    break;
                case "CNBC":
                    CNBC.add(record);
                    break;
                default:
                    break;
            }
        }

        economicNewsBySource.add(BBCNews);
        economicNewsBySource.add(TheTelegraph);
        economicNewsBySource.add(TheEconomist);
        economicNewsBySource.add(TheIndependent);
        economicNewsBySource.add(CNBC);

        return economicNewsBySource;
    }
}