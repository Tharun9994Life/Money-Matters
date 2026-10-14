package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.mycompany.Records.GoodRecord;
import com.mycompany.Users.Consumer;

@Repository
public class SQLQueriesPurchase extends SQLQueries {

    // Inserts a new purchase record into the Purchase table
    public static void insertPurchase(Consumer consumer, GoodRecord good, Integer quantity) {
        String query = "INSERT INTO Purchase (ConsumerID, GoodID, QuantityBought) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumer.getConsumerID());
            pstmt.setInt(2, good.goodID());
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Calculates the total spending for a consumer today
    public static Double showTodaysSpending(Consumer consumer) {
        String query = "SELECT SUM(Goods.GoodPrice * Purchase.QuantityBought) AS TotalSpending " +
                       "FROM Purchase, Goods, Consumers " +
                       "WHERE Purchase.PurchaseDate >= ? AND Purchase.PurchaseDate < ? " +
                       "AND Goods.GoodID = Purchase.GoodID " +
                       "AND Purchase.ConsumerID = Consumers.ConsumerID " +
                       "AND Consumers.ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
            pstmt.setInt(3, consumer.getConsumerID());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalSpending");
            }
        }

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return null;
    }
}