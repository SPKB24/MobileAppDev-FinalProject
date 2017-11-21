package com.example.mikezurawski.onyourmark.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout view_monthly_limit_layout;
    LinearLayout change_monthly_limit_layout;
    TextView monthly_limit_text;
    EditText monthly_limit_input;
    Button monthly_limit_edit_btn;
    Button monthly_limit_save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        new HamburgerMenuHandler(this, R.id.toolbar_settings, "Settings", 3).init();

        view_monthly_limit_layout = findViewById(R.id.view_monthly_limit_layout);
        change_monthly_limit_layout = findViewById(R.id.change_monthly_limit_layout);
        monthly_limit_text = findViewById(R.id.monthly_limit_text);
        monthly_limit_input = findViewById(R.id.monthly_limit_input);

        monthly_limit_edit_btn = findViewById(R.id.monthly_limit_edit_btn);
        monthly_limit_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_monthly_limit_layout.setVisibility(View.GONE);
                change_monthly_limit_layout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Click Edit Btn", Toast.LENGTH_SHORT).show();
            }
        });

        monthly_limit_save_btn = findViewById(R.id.monthly_limit_save_btn);
        monthly_limit_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_monthly_limit_layout.setVisibility(View.VISIBLE);
                change_monthly_limit_layout.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Click Save Btn", Toast.LENGTH_SHORT).show();
                // Do more stuff to save the new value in SystemPrefs and display in the new text view
            }
        });
    }
}
