package com.mycompany.Records;

public class Purchase {
    private String purchaseDate;
    private String goodName;
    private double goodPrice;
    private int goodQuantity;
    private double totalPrice;

    public Purchase(String goodName, double goodPrice, int goodQuantity, String purchaseDate) {
        this.purchaseDate = purchaseDate;
        this.goodName = goodName;
        this.goodPrice = goodPrice;
        this.goodQuantity = goodQuantity;
        this.totalPrice = goodPrice * goodQuantity;
    }
    
    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public int getGoodQuantity() {
        return goodQuantity;
    }

    public void setGoodQuantity(int goodQuantity) {
        this.goodQuantity = goodQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}