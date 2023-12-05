package com.example.onestopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * FilterActivity allows the user to filter items based on various criteria such as make, date, and tags.
 */
public class FilterActivity extends AppCompatActivity {

    private EditText makeEditText, startDate, endDate, tags;
    private Button applyFilterButton;
    private ImageButton cancelButton;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        makeEditText = findViewById(R.id.make);
        tags = findViewById(R.id.tags);
        applyFilterButton = findViewById(R.id.select_button);
        cancelButton = findViewById(R.id.sort_cancel);
        resetButton = findViewById(R.id.reset_button);
        endDate = findViewById(R.id.end);
        startDate = findViewById(R.id.start);
        String prevMake = getIntent().getStringExtra("makeFilter");
        String prevstartDate = getIntent().getStringExtra("startDateFilter");
        String prevendDate = getIntent().getStringExtra("endDateFilter");
        ArrayList<String> prevTags = getIntent().getStringArrayListExtra("tagsFilter");
        Log.d("tags", "" + (prevTags == null));
        displayPreviousFilters(prevstartDate, prevendDate, prevMake, prevTags);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, startDate);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, endDate);
            }
        });
        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateStr = startDate.getText() != null ? startDate.getText().toString() : "";
                String endDateStr = endDate.getText() != null ? endDate.getText().toString() : "";
                String makeStr = makeEditText.getText() != null ? makeEditText.getText().toString() : "";
                ArrayList<String> tagsArr = new ArrayList<>();

                if (tags.getText() != null && !tags.getText().toString().isEmpty()) {
                    // Split the string using commas and add to the ArrayList
                    String[] tagsArray = tags.getText().toString().split(",");
                    tagsArr.addAll(Arrays.asList(tagsArray));
                } else {
                    // Handle the case where the EditText is null
                    tagsArr = new ArrayList<>();
                }
                if(validDate(startDateStr, endDateStr)){
                    if(!(TextUtils.isEmpty(startDateStr) && TextUtils.isEmpty(endDateStr) && TextUtils.isEmpty(makeStr) && tagsArr.size() == 0)) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("startDate", startDateStr);
                        resultIntent.putExtra("endDate", endDateStr);
                        resultIntent.putExtra("make", makeStr);
                        resultIntent.putStringArrayListExtra("tags", tagsArr);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(FilterActivity.this, "At least one filter must be applied!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("clear", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Displays the previous filter values in the corresponding UI elements.
     *
     * @param prevStartDate The previous start date filter value.
     * @param prevEndDate   The previous end date filter value.
     * @param prevMake       The previous make filter value.
     * @param prevTags       The previous tags filter values.
     */
    private void displayPreviousFilters(String prevStartDate, String prevEndDate, String prevMake, ArrayList<String> prevTags) {
        if(!TextUtils.isEmpty(prevStartDate)){
            startDate.setText(prevStartDate);
        }
        if(!TextUtils.isEmpty(prevEndDate)){
            endDate.setText(prevEndDate);
        }
        if(!TextUtils.isEmpty(prevMake)){
            makeEditText.setText(prevMake);
        }
        if(prevTags != null && prevTags.size() > 0){
            tags.setText(String.join(",", prevTags));
        }
    }

    /**
     * Shows the date picker dialog for selecting a date.
     *
     * @param v        The View that triggered the method (in this case, a button click).
     * @param editText The EditText where the selected date should be displayed.
     */
    public void showDatePickerDialog(View v, EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the EditText with the selected date
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    editText.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    /**
     * Validates the selected start and end dates.
     *
     * @param startDateStr The selected start date as a string.
     * @param endDateStr   The selected end date as a string.
     * @return True if the dates are valid; false otherwise.
     */
    public boolean validDate(String startDateStr, String endDateStr) {
        Log.d("In ValidDate", startDateStr + endDateStr);
        final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        if((startDateStr == null || startDateStr.isEmpty()) && (endDateStr == null || endDateStr.isEmpty())) {
            return true;
        }
        else if((startDateStr == null || startDateStr.isEmpty()) && (endDateStr != null && !endDateStr.isEmpty())) {
            Toast.makeText(this, "One date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if((startDateStr != null || !startDateStr.isEmpty()) && (endDateStr == null && endDateStr.isEmpty())) {
            Toast.makeText(this, "One date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {

            // Parse the date strings and compare timestamps
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(DATE_FORMATTER.parse(startDateStr));
            Calendar afterDate = Calendar.getInstance();
            afterDate.setTime(DATE_FORMATTER.parse(endDateStr));
            Log.d("In Date FA", ""+startDate.before(afterDate));
            // Compare dates
            if(!startDate.before(afterDate)) {
                Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            }
            return startDate.before(afterDate);
        } catch (Exception e) {
            // invalid date format
            Toast.makeText(this, "Invalid Format", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}