package com.mycompany.Repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.stereotype.Repository;

import com.mycompany.Algorithms.FileHandling;
import com.mycompany.PythonFlaskAPI.APIQueue;

@Repository
public class SQLQueriesDDL extends SQLQueries {

    private static final APIQueue taskManager = new APIQueue(4);

    // Creates the Consumers table
    private static void createConsumerTable() {
        File file = new File("SQLCreateTableQueries/ConsumersTable.txt");

        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the Producers table
    private static void createProducerTable() {
        File file = new File("SQLCreateTableQueries/ProducersTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the EconomyRater table
    private static void createEconomyRaterTable() {
        File file = new File("SQLCreateTableQueries/EconomyRaterTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the Goods table
    private static void createGoodsTable() {
        File file = new File("SQLCreateTableQueries/GoodsTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the Purchase table
    private static void createPurchaseTable() {
        File file = new File("SQLCreateTableQueries/PurchaseTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the FactorInput table
    private static void createFactorInputTable() {
        File file = new File("SQLCreateTableQueries/FactorInputTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the Stocks table
    private static void createStocksTable() {
        File file = new File("SQLCreateTableQueries/StocksTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creates the Trades table
    private static void createTradesTable() {
        File file = new File("SQLCreateTableQueries/TradesTable.txt");
        FileHandling createTableTXTGetter = new FileHandling();
        String query = createTableTXTGetter.readFile(file);

        try (Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {     
            pstmt.executeUpdate();
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Public method to create all tables in the correct order
    public static void createTables() {
        taskManager.submitTask(() -> {
            createConsumerTable();
            createEconomyRaterTable();
            createGoodsTable();
            createPurchaseTable();
            createProducerTable();
            createFactorInputTable();
            createStocksTable();
            createTradesTable();
        });
    }
}