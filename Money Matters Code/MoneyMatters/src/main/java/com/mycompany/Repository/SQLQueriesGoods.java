package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class SQLQueriesGoods extends SQLQueries {

    // Inserts a new good into the Goods table
    public static void insertGood(String goodName, Double goodPrice) {
        String query = "INSERT INTO Goods (GoodName, GoodPrice) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, goodName);
            pstmt.setDouble(2, goodPrice);

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Checks if a good with the given name and price already exists
    public static boolean doesGoodIDExist(String goodName, Double goodPrice) {
        String query = "SELECT COUNT(*) AS Count FROM Goods WHERE GoodName = ? AND GoodPrice = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, goodName);
            pstmt.setDouble(2, goodPrice);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("Count");
                return count >= 1;
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return false;
    }

    // Retrieves the GoodID for a specific good by name and price
    public static int getGoodID(String goodName, Double goodPrice) {
        String query = "SELECT GoodID FROM Goods WHERE GoodName = ? AND GoodPrice = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, goodName);
            pstmt.setDouble(2, goodPrice);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int goodID = rs.getInt("GoodID");
                return goodID;
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return -1;
    }
}