package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class BudgetItem implements Serializable {

    private int _id;
    private String _location = "";
    private Date _date;
    private ArrayList<CostItem> _items = new ArrayList<>();

    public BudgetItem() {
        this._id = 1; // TODO: bad
    }

    public BudgetItem setLocation(final String location) {
        this._location = location;
        return this;
    }

    public BudgetItem setDate(final String date) {
        // TODO: Do magical string date to object date conversion here
        this._date = new Date();
        return this;
    }

    public BudgetItem addItem(final String item_title, final Double item_cost) {
        return addItem(new CostItem().setTitle(item_title).setCost(item_cost));
    }

    public BudgetItem addItem(final CostItem item) {
        this._items.add(item);
        return this;
    }

    public String getLocation() {
        return this._location;
    }

    public Date getDate() {
        return this._date;
    }

    public ArrayList<CostItem> getItems() {
        return this._items;
    }

    public int getID() {
        return this._id;
    }
}
