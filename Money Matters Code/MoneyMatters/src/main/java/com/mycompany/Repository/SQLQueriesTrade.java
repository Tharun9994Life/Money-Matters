package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

@Repository
public class SQLQueriesTrade extends SQLQueries {

    //Inserts a trade (buy or sell) into the Trades table. Can handle trades for either a consumer or a producer.
    public static void insertTrade(Consumer consumer, Producer producer, Integer stockID, String tradeType, Double askPrice, Integer quantity) {
        int userID;
        String query;

        // Determine whether this trade is for a producer or a consumer
        if (consumer == null) {
            userID = producer.getProducerID();
            query = "INSERT INTO Trades (ProducerID, StockID, TradeType, AskPrice, Quantity) VALUES (?, ?, ?, ?, ?)";
        } 
        
        else {
            userID = consumer.getConsumerID();
            query = "INSERT INTO Trades (ConsumerID, StockID, TradeType, AskPrice, Quantity) VALUES (?, ?, ?, ?, ?)";
        }

        // Execute the trade insertion
        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userID);
            pstmt.setInt(2, stockID);
            pstmt.setString(3, tradeType);
            pstmt.setDouble(4, askPrice);
            pstmt.setInt(5, quantity);
            pstmt.executeUpdate();
        } 
        
        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    //Retrieves the current stock holdings for a user (consumer or producer). Calculates net quantity, total cost, average ask price, and profit per stock.
    public static ArrayList<ArrayList<Object>> getCurrentUserStock(Consumer consumer, Producer producer) {
        int userID;
        String query;
        ArrayList<ArrayList<Object>> trades = new ArrayList<>();

        // Build the correct query depending on user type
        if (consumer == null) {
            userID = producer.getProducerID();
            query = "SELECT Stocks.StockName, " +
                    "SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1)) AS NetQuantity, " +
                    "SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1) * Trades.AskPrice) AS TotalAskPrice, " +
                    "AVG(Trades.AskPrice) AS AverageAskPrice, " +
                    "AVG(Stocks.AskPrice) - AVG(Trades.AskPrice) AS Profit " +
                    "FROM Trades, Stocks, Producers " +
                    "WHERE Stocks.StockID = Trades.StockID AND Producers.ProducerID = ? AND Producers.ProducerID = Trades.ProducerID " +
                    "GROUP BY Trades.ProducerID, Stocks.StockName " +
                    "HAVING SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1)) > 0 " +
                    "ORDER BY Trades.ProducerID, Stocks.StockName";
        } 
        
        else {
            userID = consumer.getConsumerID();
            query = "SELECT Stocks.StockName, " +
                    "SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1)) AS NetQuantity, " +
                    "SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1) * Trades.AskPrice) AS TotalAskPrice, " +
                    "AVG(Trades.AskPrice) AS AverageAskPrice, " +
                    "AVG(Stocks.AskPrice) - AVG(Trades.AskPrice) AS Profit " +
                    "FROM Trades, Stocks, Consumers " +
                    "WHERE Stocks.StockID = Trades.StockID AND Consumers.ConsumerID = ? AND Consumers.ConsumerID = Trades.ConsumerID " +
                    "GROUP BY Trades.ConsumerID, Stocks.StockName " +
                    "HAVING SUM(Trades.Quantity * IIF(Trades.TradeType = 'BUY', 1, -1)) > 0 " +
                    "ORDER BY Trades.ConsumerID, Stocks.StockName";
        }

        // Execute query and populate results
        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ArrayList<Object> tempArrayList = new ArrayList<>();
                tempArrayList.add(rs.getString("StockName"));
                tempArrayList.add(rs.getInt("NetQuantity"));
                tempArrayList.add(rs.getDouble("TotalAskPrice"));
                tempArrayList.add(rs.getDouble("AverageAskPrice"));
                tempArrayList.add(rs.getDouble("Profit"));
                trades.add(tempArrayList);
            }
        } 
        
        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return trades;
    }
}