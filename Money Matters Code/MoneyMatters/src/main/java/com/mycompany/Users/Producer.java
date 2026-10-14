package com.mycompany.Users;

public class Producer extends User {

    private Integer producerID;
    
    public Integer getProducerID() {
        return producerID;
    }

    public void setProducerID(Integer producerID) {
        this.producerID = producerID;
    }

    public Producer () {

    }

    public Producer(String firstName, String lastName, int age, String dateOfBirth, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Producer(int producerID, String firstName, String lastName, int age, String dateOfBirth, String email, String phoneNumber, String password) {
        this.producerID = producerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Producer(int producerID, String firstName, String lastName, int age, String dateOfBirth, String email, String phoneNumber, String password, Double stockBudget) {
        this.producerID = producerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.stockBudget = stockBudget;
    }
}