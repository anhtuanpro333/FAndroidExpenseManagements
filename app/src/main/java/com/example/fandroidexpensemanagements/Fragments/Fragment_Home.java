package com.example.fandroidexpensemanagements.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Interface.UserMoney;
import com.example.fandroidexpensemanagements.Interface.OnCompleteListener;
import com.example.fandroidexpensemanagements.Model.ExtraBudget;
import com.example.fandroidexpensemanagements.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Home extends Fragment {

    Toolbar home_toolbar;
    TextView home_todayUse, todayPercent, todayCondition,
            yourBalance, monthIncome, extraMoney,
            monthlyExpenses, expenses, totalExpenses;
    RecyclerView home_Recyclerview;
    RelativeLayout home_logo;
    FirebaseUser user;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__home, container, false);
        home_toolbar = view.findViewById(R.id.home_toolbar);

        home_todayUse = view.findViewById(R.id.home_todayUse);
        todayPercent = view.findViewById(R.id.todayPercent);
        todayCondition = view.findViewById(R.id.todayCondition);
        yourBalance = view.findViewById(R.id.yourBalance);
        monthIncome = view.findViewById(R.id.monthIncome);
        extraMoney = view.findViewById(R.id.extraMoney);
        monthlyExpenses = view.findViewById(R.id.monthlyExpenses);
        expenses = view.findViewById(R.id.expenses);
        totalExpenses = view.findViewById(R.id.totalExpenses);
        home_Recyclerview = view.findViewById(R.id.home_Recyclerview);
        home_logo = view.findViewById(R.id.home_logo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(home_toolbar);
        activity.setTitle("");

//        getExtraIncome( new OnCompleteListener() {
//            @Override
//            public void onGetIncomeComplete(int income) {
//                extraMoney.setText("$ " +income);
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.deletedAllExpenses:
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference expenseReference = reference.child("Expenses");
                expenseReference.removeValue();
                Toast.makeText(getActivity(), "All expenses have been deleted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeMonthInCome:
                Toast.makeText(getActivity(), "changeMonthInCome", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeExpenses:
                Toast.makeText(getActivity(), "changeExpenses", Toast.LENGTH_SHORT).show();
                break;
            case R.id.days:
                Toast.makeText(getActivity(), "days", Toast.LENGTH_SHORT).show();
                break;
            case R.id.weeks:
                Toast.makeText(getActivity(), "weeks", Toast.LENGTH_SHORT).show();
                break;
            case R.id.months:
                Toast.makeText(getActivity(), "months", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getExtraIncome(final OnCompleteListener onCompleteListener) {
        final int[] money = {0};
        DatabaseReference reference = FirebaseDatabase.getInstance().
                getReference().child("ExtraBudgets");
        reference.orderByChild("userID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ExtraBudget user = dataSnapshot.getValue(ExtraBudget.class);
                    Log.d("id", user.getUserID()!=null ? "no":"yes");
                    if (user.getUserID().equals(id)) {
                        Toast.makeText(getActivity(), user.getUserMoney() + "", Toast.LENGTH_SHORT).show();
                        money[0] = money[0] + user.getUserMoney();
                    }
                }
                onCompleteListener.onGetIncomeComplete(money[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("money",money[0]+"");
        return money[0];
    }
}