package database;

import java.io.Serializable;

/**
 * Created by sohit on 11/10/17.
 */

public class CostItem implements Serializable {

    private String _title = "";
    private Double _cost = null;

    public CostItem() {}

    public CostItem setTitle(final String item_title) {
        this._title = item_title;
        return this;
    }

    public CostItem setCost(final Double item_cost) {
        this._cost = item_cost;
        return this;
    }

    public String getTitle() {
        return this._title;
    }

    public Double getCost() {
        return this._cost;
    }
}
