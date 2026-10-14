package com.mycompany.Repository;

import org.springframework.stereotype.Repository;

import com.mycompany.Users.User;

@Repository
public abstract class SQLQueriesUsers<T extends User> extends SQLQueries {
    
    // Inserts a new user record into the users table
    public abstract void insertUserDataIntoTable(T user);
    
    // Retrieves a user record by ID
    public abstract T getData(int userID);
    
    // Authenticates a user login by checking email and password
    public abstract int authenticateLogin(String email, String password);
    
    // Checks if an email already exists in the database
    public abstract int doesEmailExist(String email);

    // Updates the user's password
    public abstract void updatePassword(String password, int userID);

    // Updates the user's stock budget
    public abstract void updateStockBudget(T user, Double stockBudget);

    // Checks if the user needs a stock budget update and updates
    public abstract void needToUpdateStockBudget(T user);
}