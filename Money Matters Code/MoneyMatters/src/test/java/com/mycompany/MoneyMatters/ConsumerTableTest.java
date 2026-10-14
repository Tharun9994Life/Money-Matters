package com.mycompany.MoneyMatters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mycompany.Repository.SQLQueriesConsumers;
import com.mycompany.Users.Consumer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mockito.MockedStatic;

public class ConsumerTableTest {
    private SQLQueriesConsumers sqlConsumers;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() throws Exception {
        sqlConsumers = new SQLQueriesConsumers();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testInsertUserDataIntoTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Consumer consumer = new Consumer(2207, "Tharun", "Nadeswaran", 17, "2008-01-30",
                                             "testemail@gmail.com", "+44 9535 134533", "password",
                                             5000.00, 2000.00, null, "Monthly", 0.00, 10000.00);

            sqlConsumers.insertUserDataIntoTable(consumer);

            verify(mockPreparedStatement, times(1)).setString(1, "Tharun");
            verify(mockPreparedStatement, times(1)).setString(2, "Nadeswaran");
            verify(mockPreparedStatement, times(1)).setInt(3, 17);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement).close();
            verify(mockConnection).close();
        }
    }

    @Test
    void testGetData() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            // Mock PreparedStatement and ResultSet
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock ResultSet
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("ConsumerID")).thenReturn(2207);
            when(mockResultSet.getString("FirstName")).thenReturn("Tharun");
            when(mockResultSet.getString("LastName")).thenReturn("Nadeswaran");
            when(mockResultSet.getInt("Age")).thenReturn(17);
            when(mockResultSet.getString("DateOfBirth")).thenReturn("2008-01-30");
            when(mockResultSet.getString("Email")).thenReturn("testemail.com");
            when(mockResultSet.getString("PhoneNumber")).thenReturn("+44 9535 134533");
            when(mockResultSet.getString("Password")).thenReturn("password");
            when(mockResultSet.getDouble("BudgetSize")).thenReturn(5000.00);
            when(mockResultSet.getDouble("CurrentBudget")).thenReturn(2000.00);
            when(mockResultSet.getDate("LastRenewed")).thenReturn(java.sql.Date.valueOf("2025-01-01"));
            when(mockResultSet.getString("FrequencyOfRenewal")).thenReturn("Monthly");
            when(mockResultSet.getDouble("SustainableSpending")).thenReturn(0.00);
            when(mockResultSet.getDouble("StockBudget")).thenReturn(10000.00);

            Consumer consumer = sqlConsumers.getData(2207);

            assertNotNull(consumer);
            assertEquals("Tharun", consumer.getFirstName());
            assertEquals(2207, consumer.getConsumerID());

            verify(mockPreparedStatement).setInt(1, 2207);
            verify(mockPreparedStatement).close();
            verify(mockConnection).close();
        }
    }
}