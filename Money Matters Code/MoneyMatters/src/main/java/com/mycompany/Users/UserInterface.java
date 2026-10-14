package com.mycompany.Users;

public interface UserInterface {
    
    public String getFirstName();
    public void setFirstName(String firstName);
    
    public String getLastName();
    public void setLastName(String lastName);
    
    public Integer getAge();
    public void setAge(Integer age);
    
    public String getDateOfBirth();
    public void setDateOfBirth(String dateOfBirth);
    
    public String getEmail();
    public void setEmail(String email);
    
    public String getPassword();
    public void setPassword(String password);

    public String getPhoneNumber();
    public void setPhoneNumber(String phoneNumber);

    public Double getStockBudget();
    public void setStockBudget(Double stockBudget);
}