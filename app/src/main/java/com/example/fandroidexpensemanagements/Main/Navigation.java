package com.example.fandroidexpensemanagements.Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Adapters.Home_ViewPager_Adapter;
import com.example.fandroidexpensemanagements.Fragments.Fragment_Chart;
import com.example.fandroidexpensemanagements.Fragments.Fragment_Expenses;
import com.example.fandroidexpensemanagements.Fragments.Fragment_Home;
import com.example.fandroidexpensemanagements.Fragments.Fragment_User;
import com.example.fandroidexpensemanagements.MainActivity;
import com.example.fandroidexpensemanagements.Model.Expense;
import com.example.fandroidexpensemanagements.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Navigation extends AppCompatActivity {
    ViewPager2 navigate_pages;
    ImageButton home, chart, expense, user;
    FloatingActionButton addExpenses;
    ArrayList<Fragment> manager;
    Home_ViewPager_Adapter viewPager_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        navigate_pages = findViewById(R.id.navigate_pagers);
        home = findViewById(R.id.home_navigate);
        chart = findViewById(R.id.chart_navigate);
        expense = findViewById(R.id.expenses_navigate);
        user = findViewById(R.id.user_navigate);
        addExpenses = findViewById(R.id.addExpense);

        manager = new ArrayList<>();
        manager.add(new Fragment_Home());
        manager.add(new Fragment_Chart());
        manager.add(new Fragment_Expenses());
        manager.add(new Fragment_User());

        viewPager_adapter = new Home_ViewPager_Adapter(this, manager);
        navigate_pages.setAdapter(viewPager_adapter);

        navigate_pages.setUserInputEnabled(false);

        home.setOnClickListener(view -> {
            changePages(home, chart, expense, user);
            navigate_pages.setCurrentItem(0);
        });

        chart.setOnClickListener(view -> {
            changePages(chart, home, expense, user);
            navigate_pages.setCurrentItem(1);
        });

        expense.setOnClickListener(view -> {
            changePages(expense, home, chart, user);
            navigate_pages.setCurrentItem(2);
        });

        user.setOnClickListener(view -> {
            changePages(user, home, chart, expense);
            navigate_pages.setCurrentItem(3);
        });

        addExpenses.setOnClickListener(view -> addExpenses());
    }

    public void changePages(ImageButton press, ImageButton... button) {
        press.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        for (int i = 0; i < button.length; i++) {
            button[i].setColorFilter(ContextCompat.getColor(this, R.color.Crimson), PorterDuff.Mode.SRC_IN);
        }
    }

    public void addExpenses() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String id = firebaseUser.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_add_expenses_dialog, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText names = view.findViewById(R.id.et_nameExpenses);
        EditText days = view.findViewById(R.id.et_dayExpenses);
        EditText moneies = view.findViewById(R.id.et_moneyExpenses);
        ImageView expand = view.findViewById(R.id.expand);
        Button add = view.findViewById(R.id.btn_addExpense);

        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeActiviy(new AddMultyExpenses());
            }
        });

        days.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (days.getRight() - days.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Navigation.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                // Set the date chosen by the user
                                days.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day); // set initial date to 15/04/2023 (YYYY, MM, DD)
                        datePickerDialog.show();
                        return true;
                    }
                }
                return false;
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = names.getText().toString().trim();
                String day = days.getText().toString().trim();
                String moneyString = moneies.getText().toString().trim();
                if (name.length() > 0 && !moneyString.isEmpty() && checkDayFormat(days)) {
                    int money = Integer.parseInt(moneyString);
                    Expense expense1 = new Expense(id, name, money, day);
                    database.child("Expenses").push().setValue(expense1);
                    dialog.dismiss();
                } else {
                    Toast.makeText(Navigation.this, "Please check your input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void changeActiviy(AppCompatActivity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

    public boolean checkDayFormat(EditText days) {
        String day = days.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); // không chấp nhận ngày tháng không hợp lệ (ví dụ 31/02/2023)
        try {
            Date date = dateFormat.parse(day);
            return true;
        } catch (ParseException e) {
            days.setText("Ex: 21/1/2023");
            return false;
        }
    }

}