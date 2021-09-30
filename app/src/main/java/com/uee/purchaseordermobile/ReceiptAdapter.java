package com.uee.purchaseordermobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {

    Context context;
    List<String> id;
    List<String> name;
    List<Double> quantity;
    List<Double> price;



    public ReceiptAdapter(Context ct, List<String> id, List<String> name, List<Double> quantity, List<Double> price){
        this.context = ct;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.table_item, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Double totalPrice = quantity.get(position).intValue() *price.get(position);

        holder.name.setText(name.get(position));
        holder.quantity.setText(String.valueOf(quantity.get(position).intValue()));
        holder.price.setText(String.format("%.2f",price.get(position)));
        holder.total.setText(String.format("%.2f",totalPrice));

    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, quantity, price,total;


        public ViewHolder(@NonNull View items){
            super(items);

            name = items.findViewById(R.id.table_item_name);
            total = items.findViewById(R.id.table_item_totPrice);
            price = items.findViewById(R.id.table_item_unitPrice);
            quantity = items.findViewById(R.id.table_item_quantity);

        }

    }
}
