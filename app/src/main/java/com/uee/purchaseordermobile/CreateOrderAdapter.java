package com.uee.purchaseordermobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CreateOrderAdapter extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder> {

    Context context;
    List<String> id;
    List<String> supplierID;
    List<String> name;
    List<String> description;
    List<String> measurement;
    List<Double> price;


    public CreateOrderAdapter(Context ct, List<String> id, List<String> supplierID, List<String> name,  List<String> description,  List<String> measurement,  List<Double> price){
        this.context = ct;
        this.id = id;
        this.supplierID = supplierID;
        this.name = name;
        this.description = description;
        this.measurement = measurement;
        this.price = price;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card_view, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(name.get(position));
        holder.description.setText(description.get(position));
        holder.price.setText(price.get(position).toString());
        holder.measurement_display.setText(measurement.get(position).toString());

    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, description, price,measurement_display;
        EditText quantity,requiredDate,comments;
        CheckBox select;

        public ViewHolder(@NonNull View items){
            super(items);

            name = items.findViewById(R.id.itemCard_name);
            description = items.findViewById(R.id.itemCard_description);
            price = items.findViewById(R.id.itemCard_price);
            quantity = items.findViewById(R.id.itemCard_input_quantity);
            select = items.findViewById(R.id.itemCard_checkBox);
            requiredDate = items.findViewById(R.id.requiredDate_get);
            comments = items.findViewById(R.id.comment_get);
            measurement_display = items.findViewById(R.id.measurement_display);
        }

    }
}
