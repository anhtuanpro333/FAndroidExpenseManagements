package com.example.fandroidexpensemanagements.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fandroidexpensemanagements.Model.Expense;
import com.example.fandroidexpensemanagements.R;

import java.util.ArrayList;

public class ExpensesRecyclerView_Adapter extends RecyclerView.Adapter<ExpensesRecyclerView_Viewholder>{
    private Context context;
    private ArrayList<Expense> expenses;

    public ExpensesRecyclerView_Adapter(Context context, ArrayList<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpensesRecyclerView_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_expenses,null,false);
        return new ExpensesRecyclerView_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesRecyclerView_Viewholder holder, int position) {
        Expense expense = expenses.get(position);
        holder.name.setText(expense.getExpenseName());
        holder.money.setText(expense.getExpenseMoney()+"");
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}

class ExpensesRecyclerView_Viewholder extends RecyclerView.ViewHolder {
    EditText name,money;
    public ExpensesRecyclerView_Viewholder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.nameExpense);
        this.money = itemView.findViewById(R.id.moneyExpense);
    }
}
