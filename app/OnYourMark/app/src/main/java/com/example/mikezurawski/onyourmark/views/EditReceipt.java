package com.example.mikezurawski.onyourmark.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;
import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.InputMismatchException;

public class EditReceipt extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Context context;

    EditText totalTxt;
    EditText dateTxt;
    Spinner categoryChoice;

    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt);

        context = this;

        totalTxt = findViewById(R.id.editTotal);
        dateTxt = findViewById(R.id.editDate);
        categoryChoice = findViewById(R.id.mySpinner);

        new HamburgerMenuHandler(this, R.id.toolbar_settings, "Edit Receipt").init_subpage(false);

        // Set edit text field
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("total");
            if (value != null)
                totalTxt.setText(value, TextView.BufferType.EDITABLE);

            value = extras.getString("date");
            if (value != null)
                dateTxt.setText(value);
        }

        if (dateTxt.getText().toString().isEmpty()) {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dateTxt.setText(dateFormat.format(new Date()), TextView.BufferType.EDITABLE);
        }

        dateTxt.setFocusable(false);
        dateTxt.setFocusableInTouchMode(false);
        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditReceipt.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    saveToDatabase();

                    // Head back to main activity using CLEAR_TOP
                    Intent i = new Intent(EditReceipt.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } catch (InputMismatchException e) { /* Do Nothing */ }

            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void saveToDatabase() throws InputMismatchException {
        Float price;
        Date date;

        String enteredPrice = totalTxt.getText().toString();

        try {
            price = Float.parseFloat(enteredPrice);
        } catch (NumberFormatException | IllegalFormatConversionException e) {
            Toast.makeText(this, "Invalid price input", Toast.LENGTH_SHORT).show();
            throw new InputMismatchException();
        }

        String enteredDate = dateTxt.getText().toString();

        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            format.setLenient(false);
            date = format.parse(enteredDate);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date input", Toast.LENGTH_SHORT).show();
            throw new InputMismatchException();
        }

        BudgetItem newItem = new BudgetItem();
        newItem.setCost(price);
        newItem.setDate(date);
        System.out.println("******************************");
        System.out.println("ADDING CATEGORY FROM SPINNER : " + categoryChoice.getSelectedItemPosition());
        System.out.println("******************************");
        newItem.setCategory(categoryChoice.getSelectedItemPosition());

        System.out.println("");
        System.out.println("******************************");
        System.out.println("id: " + newItem.getId());
        System.out.println("category: " + newItem.getCategory());
        System.out.println("date: " + newItem.getDate());
        System.out.println("cost: " + String.format("%.2f", newItem.getCost()));
        System.out.println("******************************");


        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addBudgetItem(newItem);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateTxt.setText(String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth, year));
    }
}
