package com.example.onestopshop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterOrSort extends AppCompatActivity {

    private EditText makeEditText, dateBefore, dateAfter;
    private AppCompatButton applyFilterButton;
    private String currentFilterText = "", date_after="", date_before="";

    private ImageButton cancelButton;
    private Calendar selectedDate = Calendar.getInstance();

    private AppCompatButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_or_filter); // Make sure this layout has the necessary EditText and Button

        makeEditText = findViewById(R.id.make); // Replace with your actual EditText ID for entering the make
        applyFilterButton = findViewById(R.id.select_button);
        cancelButton = findViewById(R.id.sort_cancel);
        resetButton = findViewById(R.id.reset_button);
        dateAfter = findViewById(R.id.date_after);
        dateBefore = findViewById(R.id.date_before);


        if (!currentFilterText.isEmpty()) {
            makeEditText.setText(currentFilterText);
        }
        SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
        String savedFilterText = preferences.getString("filterText", "");
        String savedDateBefore = preferences.getString("dateBefore", "");
        String savedDateAfter = preferences.getString("dateAfter", "");
        Log.d("FilterOrSort", "Retrieved from SharedPreferences - Date Before: " + savedDateBefore + ", Date After: " + savedDateAfter);

        // Set the filter text in the EditText
        makeEditText.setText(savedFilterText);
        dateAfter.setOnClickListener(v -> showDatePickerDialog(dateAfter));
        dateBefore.setOnClickListener(v -> showDatePickerDialog(dateBefore));
//        dateAfter.setOnClickListener(v -> showDatePickerDialog(dateAfter, savedDateAfter));
//        dateBefore.setOnClickListener(v -> showDatePickerDialog(dateBefore, savedDateBefore));




        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentFilterText = makeEditText.getText().toString();
                Intent intent = new Intent(FilterOrSort.this, InventoryActivity.class);
                intent.putExtra("MAKE_FILTER", currentFilterText); // Pass the make filter
                SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filterText", currentFilterText);
                editor.apply();
                date_after = dateAfter.getText().toString();
                date_before = dateBefore.getText().toString();
                intent.putExtra("DATE_BEFORE", date_before); // dateBefore is the value from the date picker/field
                intent.putExtra("DATE_AFTER", date_after); // dateAfter is the value from the date picker/field




                startActivity(intent);

            }
        });
//        applyFilterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentFilterText = makeEditText.getText().toString();
//                date_after = dateAfter.getText().toString();
//                date_before = dateBefore.getText().toString();
//
//                Log.d("FilterOrSort", "Saving - Make: " + currentFilterText + ", Date Before: " + date_before + ", Date After: " + date_after);
//                Intent intent = new Intent(FilterOrSort.this, InventoryActivity.class);
//                intent.putExtra("MAKE_FILTER", currentFilterText); // Pass the make filter
//                intent.putExtra("DATE_BEFORE", date_before); // dateBefore is the value from the date picker/field
//                intent.putExtra("DATE_AFTER", date_after); // dateAfter is the value from the date picker/field
//
//                SharedPreferences preferences = getSharedPreferences("FilterPreferences", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("filterText", currentFilterText);
//                editor.putString("dateBefore", date_before);
//                editor.putString("dateAfter", date_after);
//                editor.apply();
//
//                startActivity(intent);
//            }
//        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditText.setText("");
            }
        });
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    updateDateEditText(dateEditText);
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));

        // Show date picker dialog
        datePickerDialog.show();
    }

//    private void showDatePickerDialog(final EditText dateEditText, String initialDate) {
//        final Calendar currentDate = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                (view, year, monthOfYear, dayOfMonth) -> {
//                    selectedDate.set(year, monthOfYear, dayOfMonth);
//                    updateDateEditText(dateEditText);
//                },
//                currentDate.get(Calendar.YEAR),
//                currentDate.get(Calendar.MONTH),
//                currentDate.get(Calendar.DAY_OF_MONTH));
//
//        // Set the initial date in the DatePickerDialog
//        if (!initialDate.isEmpty()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//            try {
//                Date parsedDate = sdf.parse(initialDate);
//                long timeInMillis = parsedDate.getTime();
//                datePickerDialog.getDatePicker().setMinDate(timeInMillis);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Show date picker dialog
//        datePickerDialog.show();
//    }

//    private void showDatePickerDialog(final EditText dateEditText, String initialDate) {
//        final Calendar currentDate = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                (view, year, monthOfYear, dayOfMonth) -> {
//                    selectedDate.set(year, monthOfYear, dayOfMonth);
//                    updateDateEditText(dateEditText);
//                },
//                currentDate.get(Calendar.YEAR),
//                currentDate.get(Calendar.MONTH),
//                currentDate.get(Calendar.DAY_OF_MONTH));
//
//        // Set the initial date in the DatePickerDialog
//        if (!initialDate.isEmpty()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//            try {
//                Date parsedDate = sdf.parse(initialDate);
//                if (parsedDate != null) {
//                    long timeInMillis = parsedDate.getTime();
//                    datePickerDialog.getDatePicker().updateDate(
//                            selectedDate.get(Calendar.YEAR),
//                            selectedDate.get(Calendar.MONTH),
//                            selectedDate.get(Calendar.DAY_OF_MONTH)
//                    );
//                    datePickerDialog.getDatePicker().setMinDate(timeInMillis);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Show date picker dialog
//        datePickerDialog.show();
//    }
//
private void showDatePickerDialog(final EditText dateEditText, String initialDate) {
    final Calendar currentDate = Calendar.getInstance();
    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            (view, year, monthOfYear, dayOfMonth) -> {
                selectedDate.set(year, monthOfYear, dayOfMonth);
                updateDateEditText(dateEditText);
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH));

    // Set the initial date in the DatePickerDialog
    if (!initialDate.isEmpty()) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date parsedDate = sdf.parse(initialDate);
            if (parsedDate != null) {
                long timeInMillis = parsedDate.getTime();
                datePickerDialog.getDatePicker().setMinDate(timeInMillis);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Show date picker dialog
    datePickerDialog.show();
}



    private void updateDateEditText(EditText dateEditText) {
        String dateFormat = "yyyy-MM-dd"; // You can change the format as needed
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateEditText.setText(sdf.format(selectedDate.getTime()));
    }


}