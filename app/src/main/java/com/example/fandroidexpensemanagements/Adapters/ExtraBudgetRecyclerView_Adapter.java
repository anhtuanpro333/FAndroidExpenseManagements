package com.example.fandroidexpensemanagements.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fandroidexpensemanagements.Model.ExtraBudget;
import com.example.fandroidexpensemanagements.Model.MonthlyBudget;
import com.example.fandroidexpensemanagements.R;

import java.util.ArrayList;

public class ExtraBudgetRecyclerView_Adapter extends RecyclerView.Adapter<ExtraBudgetRecyclerView_Viewholder>{
    private Context context;
    ArrayList<ExtraBudget> budget;
    int color;
    int image;

    public ExtraBudgetRecyclerView_Adapter(Context context, ArrayList<ExtraBudget> budget, int backgroundColor, int image) {
        this.context = context;
        this.budget = budget;
        this.color = backgroundColor;
        this.image = image;
    }

    @NonNull
    @Override
    public ExtraBudgetRecyclerView_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_budget,null,false);
        return new ExtraBudgetRecyclerView_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraBudgetRecyclerView_Viewholder holder, int position) {
        holder.budget_holder.setBackgroundColor(context.getResources().getColor(color));
        ExtraBudget extraBudget = budget.get(position);
        int idColor = color==R.color.Peach ? R.color.Coral:R.color.white;
        int moneyColor = color==R.color.Peach ? R.color.black:R.color.white;
        holder.name.setText(extraBudget.getName());
        holder.money.setText(extraBudget.getMoney()+"");
        holder.money.setTextColor(context.getResources().getColor(moneyColor));
        holder.userID.setText(extraBudget.getUserID());
        holder.userID.setTextColor(context.getResources().getColor(idColor));
        holder.place.setText(extraBudget.getPlace());
        holder.logo.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return budget.size();
    }
}

class ExtraBudgetRecyclerView_Viewholder extends RecyclerView.ViewHolder {
    TextView name,money,place,userID;
    ImageView logo;
    RelativeLayout budget_holder;
    public ExtraBudgetRecyclerView_Viewholder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.budget_Name);
        this.money = itemView.findViewById(R.id.budget_Money);
        this.userID = itemView.findViewById(R.id.budget_userId);
        this.place = itemView.findViewById(R.id.budget_place);
        this.logo = itemView.findViewById(R.id.budget_logo);
        this.budget_holder = itemView.findViewById(R.id.budget_holder);
    }
}
