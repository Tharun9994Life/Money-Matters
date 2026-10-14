package com.mycompany.Repository;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.mycompany.Algorithms.FileHandling;
import com.mycompany.Users.Producer;

@Repository
public class SQLQueriesProducers extends SQLQueriesUsers<Producer> {
    
    // Inserts a new producer into the Producers table
    @Override
    public void insertUserDataIntoTable(Producer producer) {
        String query = "INSERT INTO Producers (FirstName, LastName, Age, DateOfBirth, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, producer.getFirstName());
            pstmt.setString(2, producer.getLastName());
            pstmt.setInt(3, producer.getAge());
            pstmt.setDate(4, Date.valueOf(producer.getDateOfBirth()));
            pstmt.setString(5, producer.getEmail());
            pstmt.setString(6, producer.getPhoneNumber());
            pstmt.setString(7, producer.getPassword());

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Retrieves a producer by their ID
    @Override
    public Producer getData(int producerID) {
        String query = "SELECT * FROM Producers WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, producerID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Producer producer = new Producer(
                    rs.getInt("ProducerID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getInt("Age"),
                    rs.getString("DateOfBirth"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password"),
                    rs.getDouble("StockBudget")
                );

                return producer;
            } 
        }

        catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        
        return null;
    }

    // Authenticates login by email and password
    @Override
    public int authenticateLogin(String email, String password) {
        String query = "SELECT * FROM Producers WHERE Email = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int producerID = rs.getInt("ProducerID");
                return producerID;
            }
        } 

        catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        return -1; 
    }

    // Checks if an email already exists in the Producers table
    @Override
    public int doesEmailExist (String email) {
        String query = "SELECT Count(Email) AS Emails FROM Producers WHERE Email = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Emails");
            }
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return 0;
    }

    // Updates the password for a producer
    @Override
    public void updatePassword(String password, int producerID) {
        String query = "UPDATE Producers SET Password = ? WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, password);
            pstmt.setInt(2, producerID);
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the stock budget for a producer
    @Override
    public void updateStockBudget(Producer producer, Double stockBudget) {
        String query = "UPDATE Producers SET StockBudget = ? WHERE ProducerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, stockBudget);
            pstmt.setInt(2, producer.getProducerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Checks if a producer needs a stock budget update based on external query
    @Override
    public void needToUpdateStockBudget(Producer producer) {
        File file = new File("SQLSelectTableQueries/SelectNoNetQuantityProducer.txt");

        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) { 
                
            pstmt.setInt(1, producer.getProducerID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Update stock budget if condition is met
                updateStockBudget(producer, 1000.00);
            }
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}