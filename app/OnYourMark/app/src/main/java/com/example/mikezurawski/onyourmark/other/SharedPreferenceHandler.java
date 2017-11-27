package com.example.mikezurawski.onyourmark.other;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sohit on 11/22/17.
 */

public class SharedPreferenceHandler {

    private SharedPreferences preferences;

    private final String SHARED_PREFS_KEY = "on_your_mark_prefs";
    private final String SHARED_PREFS_MONTHLY_LIMIT_KEY = "limit";

    public SharedPreferenceHandler(final Context _context) {
        this.preferences = _context.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
    }

    public void storeFloat(final Float input) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putFloat(SHARED_PREFS_MONTHLY_LIMIT_KEY, input);
        edit.apply();
    }

    public Float getFloat() {
        return this.preferences.getFloat(SHARED_PREFS_MONTHLY_LIMIT_KEY, 0.0f);
    }

    public String getFloatAsString() {
        Float toReturn = this.preferences.getFloat(SHARED_PREFS_MONTHLY_LIMIT_KEY, 0.0f);
        return String.format("%.2f", toReturn);
    }
}
