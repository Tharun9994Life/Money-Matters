package com.mycompany.Repository;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.mycompany.Algorithms.FileHandling;
import com.mycompany.Users.Consumer;

@Repository
public class SQLQueriesConsumers extends SQLQueriesUsers<Consumer>{

    // Inserts a new consumer record into the Consumers table
    @Override
    public void insertUserDataIntoTable(Consumer consumer) {
        String query = "INSERT INTO Consumers (FirstName, LastName, Age, DateOfBirth, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, consumer.getFirstName());
            pstmt.setString(2, consumer.getLastName());
            pstmt.setInt(3, consumer.getAge());
            pstmt.setDate(4, Date.valueOf(consumer.getDateOfBirth()));
            pstmt.setString(5, consumer.getEmail());
            pstmt.setString(6, consumer.getPhoneNumber());
            pstmt.setString(7, consumer.getPassword());

            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Retrieves a consumer record by ID
    @Override
    public Consumer getData(int consumerID) {
        String query = "SELECT * FROM Consumers WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, consumerID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Consumer consumer = new Consumer(
                    rs.getInt("ConsumerID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getInt("Age"),
                    rs.getString("DateOfBirth"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password"),
                    rs.getDouble("BudgetSize"),
                    rs.getDouble("CurrentBudget"),
                    rs.getDate("LastRenewed").toLocalDate(),
                    rs.getString("FrequencyOfRenewal"),
                    rs.getDouble("SustainableSpending"),
                    rs.getDouble("StockBudget")
                );

                return consumer;
            } 

            else {
                System.out.println("No user found with ID: " + consumerID);
            }

        }

        catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }

        return null;
    }

    // Authenticates a consumer login by checking email and password
    @Override
    public int authenticateLogin(String email, String password) {
        String query = "SELECT ConsumerID FROM Consumers WHERE Email = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("ConsumerID");
            } 

            else {
                System.out.println("Invalid email or password.");
                return -1; 
            }
        } 

        catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return -1; 
        }
    }

    // Checks if an email already exists in the database
    @Override
    public int doesEmailExist (String email) {
        String query = "SELECT Count(Email) AS Emails FROM Consumers WHERE Email = ?";

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

    // Updates the consumer's password
    @Override
    public void updatePassword(String password, int consumerID) {
        String query = "UPDATE Consumers SET Password = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, password);
            pstmt.setInt(2, consumerID);
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the consumer's stock budget
    @Override
    public void updateStockBudget(Consumer consumer,  Double stockBudget) {
        String query = "UPDATE Consumers SET StockBudget = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, stockBudget);
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the consumer's overall budget size
    public void updateBudgetSize(Double budgetSize, Consumer consumer) {
        String query = "UPDATE Consumers SET BudgetSize = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, budgetSize);
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the consumer's current budget
    public  void updateCurrentBudget(Double currentBudget, Consumer consumer) {
        String query = "UPDATE Consumers SET CurrentBudget = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, currentBudget);
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the frequency of budget renewal for a consumer
    public void updateFrequencyOfBudgetRenewal(String frequencyOfRenewal, Consumer consumer) {
        String query = "UPDATE Consumers SET FrequencyOfRenewal = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, frequencyOfRenewal);
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        }

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates the last renewed date for a consumer
    public void updateLastRenewed(LocalDate lastRenewed, Consumer consumer) {
        String query = "UPDATE Consumers SET LastRenewed = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(lastRenewed));
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Updates sustainable spending for a consumer
    public void updateSustainableSpending(Double sustainableSpending, Consumer consumer) {
        String query = "UPDATE Consumers SET SustainableSpending = ? WHERE ConsumerID = ?";

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, sustainableSpending);
            pstmt.setInt(2, consumer.getConsumerID());
            pstmt.executeUpdate();
        } 

        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Checks if the consumer needs a stock budget update and updates
    @Override
    public void needToUpdateStockBudget(Consumer consumer) {
        File file = new File("SQLSelectTableQueries/SelectNoNetQuantityConsumer.txt");

        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) { 
                
            pstmt.setInt(1, consumer.getConsumerID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Update stock budget to default if condition is met
                updateStockBudget(consumer, 1000.00);
            }
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}