package com.mycompany.Repository;

public abstract class SQLQueries {

    protected static final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=MONEYMATTERSDB;encrypt=true;trustServerCertificate=true;";
    protected static final String databaseUser = "bot1";
    protected static final String databasePassword = "password";

    public String getUrl() {
        return url;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}