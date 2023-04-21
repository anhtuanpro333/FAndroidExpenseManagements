package com.example.fandroidexpensemanagements.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Adapters.ExtraBudgetRecyclerView_Adapter;
import com.example.fandroidexpensemanagements.Adapters.MonthlyBudgetRecyclerView_Adapter;
import com.example.fandroidexpensemanagements.Main.AddMultyExpenses;
import com.example.fandroidexpensemanagements.Model.ExtraBudget;
import com.example.fandroidexpensemanagements.Model.MonthlyBudget;
import com.example.fandroidexpensemanagements.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class Fragment_Expenses extends Fragment {
    DrawerLayout expenses_drawer;
    NavigationView expense_naviagtionView;
    ImageView expenses_menu;
    RecyclerView monthlyBudget, extraMoney;
    Button addBudget, addExtra;
    String userId;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ArrayList<ExtraBudget> extra;
    ArrayList<MonthlyBudget> monthly;
    MonthlyBudgetRecyclerView_Adapter monthlyAdapter;
    ExtraBudgetRecyclerView_Adapter extraAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__expenses, container, false);
        expense_naviagtionView = view.findViewById(R.id.expense_naviagtionView);
        expenses_drawer = view.findViewById(R.id.expenses_drawer);
        expenses_menu = view.findViewById(R.id.expenses_menu);
        addExtra = view.findViewById(R.id.addMExtra);
        addBudget = view.findViewById(R.id.addMBudget);

        monthlyBudget = view.findViewById(R.id.monthlyBudget);
        extraMoney = view.findViewById(R.id.extraMoney);

        monthly = new ArrayList<>();
        extra = new ArrayList<>();
        monthly = getMonthly();
        extra = getExtra();
        monthlyAdapter = new MonthlyBudgetRecyclerView_Adapter(getActivity(), monthly, R.color.Peach, R.drawable.logo_crimson);
        extraAdapter = new ExtraBudgetRecyclerView_Adapter(getActivity(), extra, R.color.Crimson, R.drawable.logo_white);

        monthlyBudget.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true));
        monthlyBudget.setAdapter(monthlyAdapter);

        extraMoney.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true));
        extraMoney.setAdapter(extraAdapter);

        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();


        expenses_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenses_drawer.openDrawer(expense_naviagtionView);
            }
        });


        addBudget.setOnClickListener((View v) -> {
            showDialog(false);
        });

        addExtra.setOnClickListener((View v) -> {
            showDialog(true);
        });

        DatabaseReference extraReference = reference.child("ExtraBudgets");
        DatabaseReference monthlyReference = reference.child("MonthlyBudget");

        expense_naviagtionView.setItemIconTintList(null);
        expense_naviagtionView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exportBalancer:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        Toast.makeText(getActivity(), "Export Balance", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.addMultyBudge:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        Toast.makeText(getActivity(), "Add multiable budge", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.addMultyExpenses:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        changeActivity(new AddMultyExpenses());
                        return true;
                    case R.id.changeSavingRule:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        Toast.makeText(getActivity(), "Chaneg saving rule", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.deleteExtra:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        extraReference.removeValue();
                        extraAdapter.notifyItemRangeChanged(0, extra.size() - 1);
                        Toast.makeText(getActivity(), "Delete extra income", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.deleteMonthly:
                        monthlyReference.removeValue();
                        monthlyAdapter.notifyItemRangeChanged(0, monthly.size() - 1);
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        Toast.makeText(getActivity(), "Delete monthly income", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.deleteAll:
                        expenses_drawer.closeDrawer(GravityCompat.END);
                        extraReference.removeValue();
                        monthlyReference.removeValue();
                        extraAdapter.notifyItemRangeChanged(0, extra.size() - 1);
                        monthlyAdapter.notifyItemRangeChanged(0, monthly.size() - 1);
                        Toast.makeText(getActivity(), "All budgets have been deleted", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        return view;
    }

    public void changeActivity(AppCompatActivity activity) {
        Intent intent = new Intent(getActivity(), activity.getClass());
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showDialog(boolean condition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_budget_dialog, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText name = view.findViewById(R.id.et_nameBudget);
        EditText money = view.findViewById(R.id.et_moneyBudget);
        EditText from = view.findViewById(R.id.et_moneyPlace);
        EditText day = view.findViewById(R.id.et_moneyDay);
        ImageView expand = view.findViewById(R.id.budget_expand);
        Button add = view.findViewById(R.id.btn_addBudget);
        reference = FirebaseDatabase.getInstance().getReference();

//        Ân hiện editText nhập date
        if (condition) {
            day.setVisibility(View.VISIBLE);
            expand.setOnClickListener((View v) -> {
                Toast.makeText(getActivity(), "Add more extra budgets", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        } else {
            day.setVisibility(View.GONE);
            expand.setOnClickListener((View v) -> {
                Toast.makeText(getActivity(), "Add more monthly budgets", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        }

//        Xử lý dữ kiện đẩy thông tin lên firebase
        add.setOnClickListener((View v) -> {
            if (condition) {
                if (checkEmpty(name, from, day) && !TextUtils.isEmpty(money.getText().toString())) {
                    String names = name.getText().toString();
                    String moneies = money.getText().toString();
                    String froms = from.getText().toString();
                    String days = day.getText().toString();
                    if (checkDayFormat(day)) {
                        ExtraBudget extraBudget = new ExtraBudget(days, Integer.parseInt(moneies), froms, userId, names);
                        reference.child("ExtraBudgets").push().setValue(extraBudget);
                        getActivity().runOnUiThread(() -> {
                            extraAdapter.notifyItemInserted(extra.size());
                        });
                        extraAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please check input", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            } else {
                if (checkEmpty(name, money, from)) {
                    String names = name.getText().toString();
                    String moneies = money.getText().toString();
                    String froms = from.getText().toString();
                    MonthlyBudget monthlyBudget = new MonthlyBudget(Integer.parseInt(moneies), froms, userId, names);
                    reference.child("MonthlyBudget").push().setValue(monthlyBudget);
                    getActivity().runOnUiThread(() -> {
                        int previousSize = monthly.size();
                        monthly = getMonthly();
                        int newItemCount = monthly.size() - previousSize;
                        if (newItemCount > 0) {
                            monthlyAdapter.notifyItemRangeInserted(previousSize, newItemCount);
                        }
                    });
                    monthlyAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please check input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public boolean checkEmpty(EditText... editTexts) {
        return Arrays.stream(editTexts).allMatch(editText -> !editText.getText().toString().isEmpty());
    }

    public boolean checkDayFormat(EditText day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = day.getText().toString();
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public ArrayList<MonthlyBudget> getMonthly() {
        ArrayList<MonthlyBudget> monthly = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("MonthlyBudget");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monthly.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MonthlyBudget budget = dataSnapshot.getValue(MonthlyBudget.class);
                    monthly.add(budget);
                }
                monthlyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "The progress seemed canceled", Toast.LENGTH_SHORT).show();
            }
        });
        return monthly;
    }

    public ArrayList<ExtraBudget> getExtra() {
        ArrayList<ExtraBudget> extra = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("ExtraBudgets");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extra.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ExtraBudget budget = dataSnapshot.getValue(ExtraBudget.class);
                    extra.add(budget);
                }
                extraAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "The progress seemed canceled", Toast.LENGTH_SHORT).show();
            }
        });
        return extra;
    }

}