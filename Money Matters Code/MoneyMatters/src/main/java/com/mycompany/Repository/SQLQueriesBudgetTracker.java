package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.mycompany.Records.Purchase;
import com.mycompany.Users.Consumer;

@Repository
public class SQLQueriesBudgetTracker extends SQLQueries {

    // Fetches the purchase prices and quantities for a consumer on a specific day (daysToSubtract from today)
    public static ArrayList<ArrayList<Double>> getLastWeekPurchases(Consumer consumer, int daysToSubtract) {
        String query = "SELECT Purchase.QuantityBought, Goods.GoodPrice " +
                       "FROM Purchase, Goods, Consumers " +
                       "WHERE Purchase.GoodID = Goods.GoodID AND Purchase.ConsumerID = Consumers.ConsumerID " +
                       "AND Consumers.ConsumerID = ? " +
                       "AND CONVERT(DATE, Purchase.PurchaseDate) = ? " +
                       "ORDER BY Purchase.PurchaseDate";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            pstmt.setInt(1, consumer.getConsumerID());
            pstmt.setString(2, LocalDate.now().minusDays(daysToSubtract).format(df));

            ArrayList<ArrayList<Double>> priceQuantityPairs = new ArrayList<>();
            ArrayList<Double> prices = new ArrayList<>();
            ArrayList<Double> quantities = new ArrayList<>();
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                prices.add(rs.getDouble("GoodPrice"));
                quantities.add(rs.getDouble("QuantityBought"));
            }

            priceQuantityPairs.add(prices);
            priceQuantityPairs.add(quantities);

            return priceQuantityPairs;
        } 

        catch (SQLException ex) {
            System.out.println("Connection failed: " + ex.getMessage());
        }

        return null;
    }

    // Retrieves all purchases of a consumer, ordered by purchase date descending
    public static ArrayList<Purchase> getConsumerPurchase(Consumer consumer) {
        String query = "SELECT Goods.GoodName, Goods.GoodPrice, Purchase.QuantityBought, Purchase.PurchaseDate " +
                       "FROM Consumers, Goods, Purchase " +
                       "WHERE Consumers.ConsumerID = Purchase.ConsumerID AND Goods.GoodID = Purchase.GoodID " +
                       "AND Consumers.ConsumerID = ? " +
                       "ORDER BY Purchase.PurchaseDate DESC";

        ArrayList<Purchase> consumerPurchases = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumer.getConsumerID());
            ResultSet rs = pstmt.executeQuery();

            // Map ResultSet rows to Purchase objects
            while (rs.next()) {
                consumerPurchases.add(new Purchase(
                    rs.getString("GoodName"),
                    rs.getDouble("GoodPrice"),
                    rs.getInt("QuantityBought"),
                    rs.getDate("PurchaseDate").toString()
                ));
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return consumerPurchases;
    }
}