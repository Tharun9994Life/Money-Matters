package com.mycompany.Repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mycompany.Algorithms.FileHandlingJSON;
import com.mycompany.PythonFlaskAPI.APIQueue;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;

@Repository
public class SQLQueriesStocks extends SQLQueries {

    private static final FileHandlingJSON JSONHandling = new FileHandlingJSON(); 
    private static final APIQueue taskManager = new APIQueue(5);

    // Inserts a new stock into the Stocks table. If the stock already exists, calls updateStock to update the price
    public static void insertStock(String stockName, Double askPrice) {
        String query = "INSERT INTO Stocks (StockName, AskPrice) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stockName);
            pstmt.setDouble(2, askPrice);
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            // If insertion fails, update the existing stock price
            updateStock(stockName, askPrice);
        }
    }

    // Reads all stock info from a JSON file and inserts them into the Stocks table
    public static void insertAllStock() {
        File tickerInfo = new File("tickerInfo.json");

        // Read JSON array of stock information
        ArrayList<LinkedHashMap<String, String>> allTickerInfoAsJSON = JSONHandling.readFileList(tickerInfo, new TypeReference<List<LinkedHashMap<String, String>>>() {});

        // Loop through all entries and insert each stock
        for (int i = 0; i < allTickerInfoAsJSON.size(); i++) {
            final String companyName = allTickerInfoAsJSON.get(i).get("companyName");
            final String tickerSymbol = allTickerInfoAsJSON.get(i).get("tickerSymbol");

            taskManager.submitTask(() -> {
                Double askPrice = PythonFlaskAPI.getAskPrice(tickerSymbol);
                insertStock(companyName, askPrice);
            });
        }
    }

    // Updates an existing stock's ask price and last updated timestamp
    public static void updateStock(String stockName, Double askPrice) {
        String query = "UPDATE Stocks SET AskPrice = ?, LastUpdated = ? WHERE StockName = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, askPrice);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, stockName);

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Retrieves the StockID of a stock by its name
    public static Integer getStockID(String stockName) {
        String query = "SELECT StockID FROM Stocks WHERE StockName = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, stockName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("StockID");
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return null;
    }
}