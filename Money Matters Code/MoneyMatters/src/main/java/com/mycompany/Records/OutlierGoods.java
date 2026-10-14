package com.mycompany.Records;

public class OutlierGoods {

    private String goodName;
    private double price;
    private double outlierInfo;

    public OutlierGoods(String goodName, double price, double outlierInfo) {
        this.goodName = goodName;
        this.price = price;
        this.outlierInfo = outlierInfo;
    }

    public String getGoodName() {
        return goodName;
    }

    public double getPrice() {
        return price;
    }

    public double getOutlierInfo() {
        return outlierInfo;
    }
}