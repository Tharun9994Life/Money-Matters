package com.mycompany.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Repository;

import com.mycompany.Records.FactorInput;
import com.mycompany.Users.Producer;

@Repository
public class SQLQueriesFactorInput extends SQLQueries {
    
    // Inserts a new FactorInput record into the database for a given producer
    public static void insertDataIntoFactorInputTable(FactorInput factorInput, Producer producer) {
        String query = "INSERT INTO FactorInput (FactorInputType, ProducerID, FactorInputName, FactorInputDescription, MarginalCost, MarginalRevenue, MarginalProfit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, factorInput.getFactorInputType());
                pstmt.setInt(2, producer.getProducerID());
                pstmt.setString(3, factorInput.getFactorInputName());
                pstmt.setString(4, factorInput.getFactorInputDescription());
                pstmt.setDouble(5, factorInput.getMarginalCost());
                pstmt.setDouble(6, factorInput.getMarginalRevenue());
                pstmt.setDouble(7, factorInput.getMarginalProfit());
                
                pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Retrieves all FactorInput records for a given producer
    public static LinkedHashMap<String, FactorInput> getAllFactorInputTable(Producer producer) {
        String query = "SELECT * FROM FactorInput WHERE ProducerID = ?";
        LinkedHashMap<String, FactorInput> factorInputs = new LinkedHashMap<>();

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, producer.getProducerID());
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    FactorInput factorInput = new FactorInput(
                        rs.getInt("FactorInputID"), 
                        rs.getString("FactorInputType"), 
                        rs.getString("FactorInputName"), 
                        rs.getString("FactorInputDescription"), 
                        rs.getDouble("MarginalCost"), 
                        rs.getDouble("MarginalRevenue")
                    );
                    
                    factorInputs.put(factorInput.getFactorInputType() + " - " + factorInput.getFactorInputName(), factorInput);
                }

                return factorInputs;
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return null;
    }

    // Retrieve a single FactorInput by name for a producer
    public static FactorInput getFactorInputByName(Producer producer, String factorInputName) {
        String query = "SELECT * FROM FactorInput WHERE ProducerID = ? AND FactorInputName = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, producer.getProducerID());
                pstmt.setString(2, factorInputName);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new FactorInput(
                        rs.getInt("FactorInputID"), 
                        rs.getString("FactorInputType"), 
                        rs.getString("FactorInputName"), 
                        rs.getString("FactorInputDescription"), 
                        rs.getDouble("MarginalCost"), 
                        rs.getDouble("MarginalRevenue")
                    );
                }
        }
    
        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return null;
    }

    // Count the number of FactorInputs of a specific type for a producer
    public static int getNumberOfFactorInputs(String factorInputType, Producer producer) {
        String query = "SELECT COUNT (FactorInputName) AS NumberOfFactorInputs FROM FactorInput WHERE FactorInputType = ? AND ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, factorInputType);
                pstmt.setInt(2, producer.getProducerID());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("NumberOfFactorInputs");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return 0;
    }

    // Get total cost for a producer
    public static double getTotalCost(int producerID) {
        String query = "SELECT SUM(MarginalCost) AS TotalCost FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("TotalCost");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Get average cost for a producer
    public static double getAverageCost(int producerID) {
        String query = "SELECT AVG(MarginalCost) AS AverageCost FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("AverageCost");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Get total revenue for a producer
    public static double getTotalRevenue(int producerID) {
        String query = "SELECT SUM(MarginalRevenue) AS TotalRevenue FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("TotalRevenue");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Get average revenue for a producer
    public static double getAverageRevenue(int producerID) {
        String query = "SELECT AVG(MarginalRevenue) AS AverageRevenue FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("AverageRevenue");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Get total profit for a producer
    public static double getTotalProfit(int producerID) {
        String query = "SELECT SUM(MarginalProfit) AS TotalProfit FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("TotalProfit");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Get average profit for a producer
    public static double getAverageProfit(int producerID) {
        String query = "SELECT AVG(MarginalProfit) AS AverageProfit FROM FactorInput WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, producerID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("AverageProfit");
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return Integer.MIN_VALUE;
    }

    // Update FactorInput's costs and revenue, and recalculate profit
    public static void updateDataIntoFactorInputTable(double marginalCost, double marginalRevenue, String factorInputName, Producer producer) {
        String query = "UPDATE FactorInput SET MarginalCost = ?, MarginalRevenue = ?, MarginalProfit = ? WHERE FactorInputName = ? AND ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setDouble(1, marginalCost);
                pstmt.setDouble(2, marginalRevenue);
                pstmt.setDouble(3, marginalRevenue - marginalCost); // Profit = Revenue - Cost
                pstmt.setString(4, factorInputName);
                pstmt.setInt(5, producer.getProducerID());
                pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Delete a FactorInput record for a producer
    public static int deleteDataFromFactorInputTable(String factorInputName, Producer producer) {
        String query = "DELETE FROM FactorInput WHERE FactorInputName = ? AND ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, factorInputName);
                pstmt.setInt(2, producer.getProducerID());
                pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return -1;
    }

    // Get all marginal costs for a producer
    public static ArrayList<Double> getAllMarginalCosts(Producer producer) {
        String query = "SELECT MarginalCost FROM FactorInput WHERE ProducerID = ?";
        ArrayList<Double> allMarginalCosts = new ArrayList<>(); 

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, producer.getProducerID());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    allMarginalCosts.add(rs.getDouble("MarginalCost"));
                }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return allMarginalCosts;
    }

    // Get all marginal revenues for a producer
    public static ArrayList<Double> getAllMarginalRevenue(Producer producer) {
        String query = "SELECT MarginalRevenue FROM FactorInput WHERE ProducerID = ?";
        ArrayList<Double> allMarginalRevenue = new ArrayList<>(); 

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, producer.getProducerID());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    allMarginalRevenue.add(rs.getDouble("MarginalRevenue"));
                }
        } 
        
        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return allMarginalRevenue;
    }
}