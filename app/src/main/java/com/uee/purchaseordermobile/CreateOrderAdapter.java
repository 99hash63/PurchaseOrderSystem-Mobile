package com.uee.purchaseordermobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    List<Float> price;

    public CreateOrderAdapter(Context ct,   List<String> id, List<String> supplierID, List<String> name,  List<String> description,  List<String> measurement,  List<Float> price){
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        public ViewHolder(@NonNull View items){
            super(items);

        }

    }
}
