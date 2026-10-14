package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Repository;

import com.mycompany.Users.Consumer;

@Repository
public class SQLQueriesRateTheEconomy extends SQLQueries {

    // Inserts a consumer rating into the EconomyRater table
    public static void insertDataIntoEconomyRaterTable(Consumer consumer, String fileName, int consumerOpinion) {
        String query = "INSERT INTO EconomyRater (ConsumerID, ConsumerOpinionFile, ConsumerOpinion) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumer.getConsumerID());
            pstmt.setString(2, fileName);
            pstmt.setInt(3, consumerOpinion);

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Returns the average consumer opinion for a given date
    public static double averageConsumerOpinion(String date) {
        String query = "SELECT AVG(ConsumerOpinion) AS AverageConsumerOpinion FROM EconomyRater WHERE ConsumerOpinionDate = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, date);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("AverageConsumerOpinion");
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return -1;
    }

    // Checks if a consumer has already rated today; returns the RatingID if exists
    public static int checkNumberOfRatings(Consumer consumer) {
        String query = "SELECT COUNT(RatingID) AS RecordNumber, RatingID FROM EconomyRater WHERE ConsumerID = ? AND ConsumerOpinionDate = ? GROUP BY RatingID";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumer.getConsumerID());
            pstmt.setString(2, LocalDate.now().toString());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("RecordNumber") == 1) {
                    return rs.getInt("RatingID");
                }
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return -1;
    }

    // Updates an existing consumer opinion in the EconomyRater table
    public static void updateConsumerOpinion(int ratingID, int consumerOpinion) {
        String query = "UPDATE EconomyRater SET ConsumerOpinion = ? WHERE RatingID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumerOpinion);
            pstmt.setInt(2, ratingID);

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Retrieves the latest consumer opinion for a given consumer
    public static LinkedHashMap<String, String> getLatestConsumerOpinion(Consumer consumer) {
        String query = "SELECT ConsumerOpinion, ConsumerOpinionDate FROM EconomyRater, Consumers " +
                       "WHERE Consumers.ConsumerID = EconomyRater.ConsumerID AND EconomyRater.ConsumerID = ? " +
                       "ORDER BY ConsumerOpinionDate DESC";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumer.getConsumerID());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                LinkedHashMap<String, String> latestConsumerOpinionMap = new LinkedHashMap<>();
                latestConsumerOpinionMap.put("consumerOpinionDate", rs.getString("ConsumerOpinionDate"));
                latestConsumerOpinionMap.put("consumerOpinion", rs.getString("ConsumerOpinion"));
                return latestConsumerOpinionMap;
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return new LinkedHashMap<>();
    }
}