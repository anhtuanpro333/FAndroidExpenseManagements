package com.example.fandroidexpensemanagements.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Model.Expense;
import com.example.fandroidexpensemanagements.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddMultyExpenses extends AppCompatActivity {
    EditText names, days, moneies;
    Button btn_addEx;
    LinearLayout layout;
    ImageView ivBack;
    FloatingActionButton addInputSpace;
    HashMap<String, EditText> viewChild;
    int count = 0;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multy_expenses);
        addInputSpace = findViewById(R.id.addInputSpace);
        layout = findViewById(R.id.virtualLayout);
        ivBack = findViewById(R.id.expandBack);
        names = findViewById(R.id.et_nameEx);
        days = findViewById(R.id.et_dayEx);
        moneies = findViewById(R.id.et_moneyEx);
        btn_addEx = findViewById(R.id.btn_addEx);

        days.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (days.getRight() - days.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50)) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddMultyExpenses.this, new DatePickerDialog.OnDateSetListener() {
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
        });

        ivBack.setOnClickListener(view -> finish());
        viewChild = new HashMap<>();
        addInputSpace.setOnClickListener(view -> {
            if (checkEmpty(names, days, moneies) && checkDayFormat(days)) {
                addMoreEditText();
            } else {
                Toast.makeText(this, "Please check all input reamin " +
                        "has empty space or input syntax", Toast.LENGTH_SHORT).show();
            }
        });

        btn_addEx.setOnClickListener(view -> {
            addExpenses(viewChild, names, days, moneies);
        });

    }

    public void addMoreEditText() {
        if (layout.getChildCount() == 0 ||
                checkEmpty(viewChild.get("text1_" + count),
                        viewChild.get("text2_" + count),
                        viewChild.get("text3_" + count))) {
            count++;
            EditText vname = new EditText(this);
            EditText vday = new EditText(this);
            EditText vmoney = new EditText(this);

            vname.setHint("Name expenses " + count);
            vday.setHint("Day expenses " + count);
            vmoney.setHint("Month expenses " + count);

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_calendar_today_24);
            vday.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
            vmoney.setInputType(moneies.getInputType());

            vday.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int DRAWABLE_RIGHT = 2;
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (vday.getRight() - vday.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(AddMultyExpenses.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                    // Set the date chosen by the user
                                    vday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                }
                            }, year, month, day); // set initial date to 15/04/2023 (YYYY, MM, DD)
                            datePickerDialog.show();
                            return true;
                        }
                    }
                    return false;
                }
            });

            vname.setLayoutParams(names.getLayoutParams());
            vday.setLayoutParams(days.getLayoutParams());
            vmoney.setLayoutParams(moneies.getLayoutParams());

            viewChild.put("text1_" + count, vname);
            viewChild.put("text2_" + count, vday);
            viewChild.put("text3_" + count, vmoney);

//            Log.d("editPut","vName: " + (vname != null ? "notNull": "null"));
//            Log.d("editPut","vDay: " + (vday != null ? "notNull": "null"));
//            Log.d("editPut","vMoney: " + (vmoney != null ? "notNull": "null"));

            vname.setBackground(null);
            vday.setBackground(null);
            vmoney.setBackground(null);

            layout.addView(vname);
            layout.addView(vday);
            layout.addView(vmoney);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public boolean checkEmpty(EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
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

    public void addExpenses(HashMap<String, EditText> viewChild, EditText... input) {
        int index = 1;
        String id = firebaseUser.getUid();
        String name = input[0].getText().toString().trim();
        String day = input[1].getText().toString().trim();
        String money = input[2].getText().toString().trim();
        if (layout.getChildCount() ==0){if (checkEmpty(input[0], input[1], input[2])) {
            Expense expense = new Expense(id,name,Integer.parseInt(money),day);
            reference.child("Expenses").push().setValue(expense);
            input[0].setText("");
            input[1].setText("");
            input[2].setText("");
        }}
        if (layout.getChildCount() > 0) {
            if (checkDayFormat(input[1])) {
                Expense expense = new Expense(id, name, Integer.parseInt(money),day);
                reference.child("Expenses").push().setValue(expense);
                input[0].setText("");
                input[1].setText("");
                input[2].setText("");
            }
            while (index <= viewChild.size()/3) {
                if (!checkEmpty(viewChild.get("text1_" + index),
                        viewChild.get("text2_" + index),
                        viewChild.get("text3_" + index))) {
                    break;
                }
                ;
                index++;
            }
            if (index > 1) {
                for (int i = 1; i < index; i++) {
                    EditText vName = viewChild.get("text1_" + i);
                    EditText vDay = viewChild.get("text2_" + i);
                    EditText vMoney = viewChild.get("text3_" + i);

                    String vname = vName.getText().toString().trim();
                    String vday = vDay.getText().toString().trim();
                    String vmoney = vMoney.getText().toString().trim();
                    int vmoneyNum = 0;
                    if (!money.equals(null)) {
                        vmoneyNum = Integer.parseInt(vmoney);
                    }
                    Expense expense = new Expense(id, vname, vmoneyNum,vday);
                    reference.child("Expenses").push().setValue(expense);
                }
                layout.removeAllViews();
                viewChild.clear();
            }
        }
    }
}