package com.mycompany.MoneyMatters;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import com.mycompany.Repository.SQLQueriesDDL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

import org.mockito.MockedStatic;

class DDLTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setup() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    void testCreateConsumerTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createConsumerTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateProducerTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createProducerTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateEconomyRaterTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createEconomyRaterTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateGoodsTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createGoodsTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreatePurchaseTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createPurchaseTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateFactorInputTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createFactorInputTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateStocksTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createStocksTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }

    @Test
    void testCreateTradesTable() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Method method = SQLQueriesDDL.class.getDeclaredMethod("createTradesTable");
            method.setAccessible(true);
            method.invoke(null);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockConnection, times(1)).close();
        }
    }
}