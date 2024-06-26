package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private int year, month, day, hour, minute;
    private DatabaseHelper databaseHelper;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        databaseHelper = new DatabaseHelper(this);
        ImageView editImageView = findViewById(R.id.editImageView);

        EditText titleEditText = findViewById(R.id.titleEditText);
        TextView dateTextView = findViewById(R.id.dateTextView);
        ImageView calendarImageView = findViewById(R.id.calendarImageView);
        TextView timeTextView = findViewById(R.id.timeTextView);
        ImageView clockImageView = findViewById(R.id.clockImageView);
        EditText contentEditText = findViewById(R.id.contentEditText);
        Button saveButton = findViewById(R.id.saveButton);



        calendarImageView.setOnClickListener(v -> showDatePickerDialog(dateTextView));
        clockImageView.setOnClickListener(v -> showTimePickerDialog(timeTextView));

        // Get taskId from Intent extras
        taskId = getIntent().getLongExtra("taskId", -1);

        // Check if taskId is valid for editing
        if (taskId != -1) {
            // Task ID is valid, retrieve task data for editing
            Task task = databaseHelper.getTaskById(taskId);
            if (task != null) {
                // Populate UI fields with task data for editing
                titleEditText.setText(task.getTitle());
                dateTextView.setText(task.getDate());
                timeTextView.setText(task.getTime());
                contentEditText.setText(task.getContent());
            }
        }

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String date = dateTextView.getText().toString().trim();
            String time = timeTextView.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            if (taskId != -1) {
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                Task task = databaseHelper.getTaskById(taskId);
                if (task != null) {
                    // Populate the fields with task details for editing
                    titleEditText.setText(task.getTitle());
                    // Populate other fields similarly
                }
            }

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                Toast.makeText(this, "Please enter title, date, and time", Toast.LENGTH_SHORT).show();
            } else {
                Task task = new Task(title, date, time, content);
                long result = databaseHelper.insertTask(task);
                if (result != -1) {
                    Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish the activity and return to the previous activity
                } else {
                    Toast.makeText(this, "Failed to save task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String date = dateTextView.getText().toString().trim();
            String time = timeTextView.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();
            String editedTitle = titleEditText.getText().toString();
            String editedDate = dateTextView.getText().toString();
            String editedTime = timeTextView.getText().toString();
            String editedContent = contentEditText.getText().toString();


            int updatedRows = databaseHelper.updateTask(taskId, editedTitle, editedDate, editedTime, editedContent);
            if (updatedRows > 0) {
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                // Return to dashboard activity
                startActivity(new Intent(this, DashboardActivity.class));
                finish(); // Close AddTaskActivity
            } else {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }


            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                Toast.makeText(this, "Please enter title, date, and time", Toast.LENGTH_SHORT).show();
            } else {
                Task task = new Task(title, date, time, content);
                long result = databaseHelper.insertTask(task);
                if (result != -1) {
                    Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish the activity and return to the previous activity
                } else {
                    Toast.makeText(this, "Failed to save task", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDatePickerDialog(TextView dateTextView) {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;

            String selectedDateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
            dateTextView.setText(selectedDateStr);
        }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TextView timeTextView) {
        final Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            this.hour = hourOfDay;
            this.minute = minute;

            String selectedTimeStr = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timeTextView.setText(selectedTimeStr);
        }, currentHour, currentMinute, true);
        timePickerDialog.show();
    }





}