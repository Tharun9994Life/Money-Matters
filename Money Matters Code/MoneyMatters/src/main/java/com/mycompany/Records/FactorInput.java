package com.mycompany.Records;

public class FactorInput {
    private int factorInputID;
    private String factorInputType;
    private String factorInputName;
	private String factorInputDescription;
    private double marginalCost;
    private double marginalRevenue;
    private double marginalProfit;

    public FactorInput() {

    }
    
    public FactorInput(String factorInputType, String factorInputName, String factorInputDescription, double marginalCost, double marginalRevenue) {
        this.factorInputType = factorInputType;
        this.factorInputName = factorInputName;
        this.factorInputDescription = factorInputDescription;
        this.marginalCost = marginalCost;
        this.marginalRevenue = marginalRevenue;
        this.marginalProfit = marginalRevenue - marginalCost;
    }

    public FactorInput(int factorInputID, String factorInputType, String factorInputName, String factorInputDescription, double marginalCost, double marginalRevenue) {
        this.factorInputID = factorInputID;
        this.factorInputType = factorInputType;
        this.factorInputName = factorInputName;
        this.factorInputDescription = factorInputDescription;
        this.marginalCost = marginalCost;
        this.marginalRevenue = marginalRevenue;
        this.marginalProfit = marginalRevenue - marginalCost;
    }

    public int getFactorInputID() {
        return factorInputID;
    }

    public void setFactorInputID(int factorInputID) {
        this.factorInputID = factorInputID;
    }

    public String getFactorInputType() {
        return factorInputType;
    }

    public void setFactorInputType(String factorInputType) {
        this.factorInputType = factorInputType;
    }

    public String getFactorInputName() {
        return factorInputName;
    }

    public void setFactorInputName(String factorInputName) {
        this.factorInputName = factorInputName;
    }

    public String getFactorInputDescription() {
        return factorInputDescription;
    }

    public void setFactorInputDescription(String factorInputDescription) {
        this.factorInputDescription = factorInputDescription;
    }

    public double getMarginalCost() {
        return marginalCost;
    }

    public void setMarginalCost(double marginalCost) {
        this.marginalCost = marginalCost;
    }

    public double getMarginalRevenue() {
        return marginalRevenue;
    }

    public void setMarginalRevenue(double marginalRevenue) {
        this.marginalRevenue = marginalRevenue;
    }

    public double getMarginalProfit() {
        return marginalProfit;
    }

    public void setMarginalProfit(double marginalProfit) {
        this.marginalProfit = marginalProfit;
    }

    @Override
    public String toString() {
        return "[factorInputID=" + factorInputID + ", factorInputType=" + factorInputType
                + ", factorInputName=" + factorInputName + ", factorInputDescription=" + factorInputDescription
                + ", marginalCost=" + marginalCost + ", marginalRevenue=" + marginalRevenue + ", marginalProfit="
                + marginalProfit + "]";
    }
}