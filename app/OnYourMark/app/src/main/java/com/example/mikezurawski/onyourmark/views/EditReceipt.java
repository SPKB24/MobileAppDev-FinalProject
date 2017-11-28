package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;

public class EditReceipt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt);

        // Set edit text field
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("total");
            EditText editText = (EditText)findViewById(R.id.editTotal);
            editText.setText(value, TextView.BufferType.EDITABLE);
        }

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditReceipt.this, "Save button clicked"
                        , Toast.LENGTH_SHORT).show();

                // TODO: Do something that saves the data
                // Head back to main activity using CLEAR_TOP
                Intent i = new Intent(EditReceipt.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditReceipt.this, "Cancel button clicked"
                        , Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });





    }
}
