package com.example.mikezurawski.onyourmark.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mikezurawski.onyourmark.R;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.example.mikezurawski.onyourmark.other.Constants;
import com.example.mikezurawski.onyourmark.other.SharedPreferenceHandler;

public class SettingsActivity extends AppCompatActivity {

    TextView monthly_limit_text;
    Button monthly_limit_edit_btn;

    Button importDbBtn;
    Button exportDbBtn;

    SharedPreferenceHandler preferences;

    Context context;
    Activity activity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        activity = this;
        preferences = new SharedPreferenceHandler(this);

        new HamburgerMenuHandler(this, R.id.toolbar_settings, "Settings", 3).init();

        monthly_limit_text = findViewById(R.id.monthly_limit_text);
        monthly_limit_edit_btn = findViewById(R.id.monthly_limit_edit_btn);
        monthly_limit_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialDialog.Builder(context)
                        .title("Change Monthly limit")
                        .inputType(InputType.TYPE_CLASS_PHONE)
                        .input("ex. 200.00", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String inputText = input.toString();
                                Float inputFloat = convertStringToFloat(inputText);

                                if (validateInputValue(inputText)) {
                                    preferences.storeFloat(inputFloat);
                                    monthly_limit_text.setText(convertFloatToString(inputFloat));
                                } else if (!inputText.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        });

        importDbBtn = findViewById(R.id.ImportDbBtn);
        importDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler.importDatabase(activity);
            }
        });

        exportDbBtn = findViewById(R.id.ExportDbBtn);
        exportDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler.exportDatabase(activity);
            }
        });

        initMonthlyDisplay();
    }

    private void initMonthlyDisplay() {
        Float monthly_limit = preferences.getFloat();
        monthly_limit_text.setText(convertFloatToString(monthly_limit));
    }

    private boolean validateInputValue(final String input) {
        Float value = convertStringToFloat(input);
        return value != null;
    }

    private Float convertStringToFloat(final String input) {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String convertFloatToString(final Float input) {
        return String.format("%.2f", input);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case Constants.REQUEST_READ_PERMISSION_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    DatabaseHandler.importDatabase(activity);
                else
                    Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show();
                return;
            }
            case Constants.REQUEST_WRITE_PERMISSION_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    DatabaseHandler.exportDatabase(activity);
                else
                    Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
