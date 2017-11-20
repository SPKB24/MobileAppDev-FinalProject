package com.example.mikezurawski.onyourmark.database;

import java.io.Serializable;
import java.util.Date;

public class BudgetItem implements Serializable {

    private String id = "";
    private String category = "";
    private Date date;
    private double cost;

    public BudgetItem() {}

    public BudgetItem(String id, String category, Date date, float cost) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
