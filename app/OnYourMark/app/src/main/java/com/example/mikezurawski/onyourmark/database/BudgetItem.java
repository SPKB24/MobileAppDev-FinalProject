package com.example.mikezurawski.onyourmark.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class BudgetItem implements Serializable {

    private String category = "";
    private Date _date;
    private float cost;

    public BudgetItem() {}

    public BudgetItem(String category, Date _date, float cost) {
        this.category = category;
        this._date = _date;
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

}
