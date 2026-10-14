package com.mycompany.Users;

import java.time.LocalDate;

public class Consumer extends User {

    private Integer consumerID;
    private Double budgetSize;
    private Double currentBudget;
    private LocalDate lastRenewed;
    private String frequencyOfBudgetRenewal;
    private Double sustainableSpending;

    public Integer getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(Integer consumerID) {
        this.consumerID = consumerID;
    }

    public Double getBudgetSize() {
        return budgetSize;
    }

    public void setBudgetSize(Double budgetSize) {
        this.budgetSize = budgetSize;
    }

    public String getFrequencyOfBudgetRenewal() {
        return frequencyOfBudgetRenewal;
    }

    public void setFrequencyOfBudgetRenewal(String frequencyOfBudgetRenewal) {
        this.frequencyOfBudgetRenewal = frequencyOfBudgetRenewal;
    }

    public Double getSustainableSpending() {
        return sustainableSpending;
    }

    public void setSustainableSpending(Double sustainableSpending) {
        this.sustainableSpending = sustainableSpending;
    }

    public Double getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(Double currentBudget) {
        this.currentBudget = currentBudget;
    }

    public LocalDate getLastRenewed() {
        return lastRenewed;
    }

    public void setLastRenewed(LocalDate lastRenewed) {
        this.lastRenewed = lastRenewed;
    }

    public Consumer () {

    }

    public Consumer(String firstName, String lastName, Integer age, String dateOfBirth, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Consumer(Integer consumerID, String firstName, String lastName, Integer age, String dateOfBirth, String email, String phoneNumber, String password) {
        this.consumerID = consumerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Consumer(Integer consumerID,  String firstName, String lastName, Integer age, String dateOfBirth, 
            String email, String phoneNumber, String password, Double budgetSize, Double currentBudget,
            LocalDate lastRenewed, String frequencyOfBudgetRenewal, Double sustainableSpending, Double stockBudget) {
        
        this.consumerID = consumerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.budgetSize = budgetSize;
        this.currentBudget = currentBudget;
        this.lastRenewed = lastRenewed;
        this.frequencyOfBudgetRenewal = frequencyOfBudgetRenewal;
        this.sustainableSpending = sustainableSpending;
        this.stockBudget = stockBudget;
    }
}