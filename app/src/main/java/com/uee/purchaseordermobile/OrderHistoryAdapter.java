package com.uee.purchaseordermobile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    Context context;
    List<String> id;
    List<String> level;
    List<String> oNum;
    List<String> site;
    List<String> supplier;
    List<Double> totItems;
    List<Double> totPrice;
    List<String> rDate;


    public OrderHistoryAdapter(Context ct, List<String> id, List<String> level,  List<String> oNum,  List<String> site,
                               List<String> supplier,  List<Double> totItems,List<Double> totPrice,List<String> rDate){
        this.context = ct;
        this.id = id;
        this.level = level;
        this.oNum = oNum;
        this.site = site;
        this.supplier = supplier;
        this.totItems = totItems;
        this.totPrice = totPrice;
        this.rDate = rDate;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_card_view, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint({"ResourceAsColor", "DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.level.setText(level.get(position));
        holder.oNum.setText("Order : #"+oNum.get(position));
        holder.site.setText("Site : "+site.get(position));
        holder.supplier.setText("Supplier : "+supplier.get(position));
        holder.totItems.setText("Total Items : "+String.valueOf(totItems.get(position).intValue()));
        holder.totPrice.setText("Total Price : LKR "+String.format("%.2f",totPrice.get(position)));
        holder.rDate.setText("Required Date : "+rDate.get(position));

        if (level.get(position).equals("Pending Approval")){
            holder.level.setTextColor(ContextCompat.getColor(context,R.color.light_black));
        }else if (level.get(position).equals("Approved")) {
            holder.level.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else if (level.get(position).equals("Declined")){
            holder.level.setTextColor(ContextCompat.getColor(context,R.color.pink_dark_red));
        }else if (level.get(position).equals("Partially Approved")){
            holder.level.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else {
            holder.level.setTextColor(ContextCompat.getColor(context,R.color.yellow));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Receipt.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("orderID",id.get(position));
                v.getContext().startActivity(intent);

            }

        });


    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView level,oNum,site,supplier,totItems,totPrice,rDate;


        public ViewHolder(@NonNull View items){
            super(items);

            level = items.findViewById(R.id.order_level_display);
            oNum = items.findViewById(R.id.order_num_display);
            site = items.findViewById(R.id.order_site_display);
            supplier = items.findViewById(R.id.order_supplier_display);
            totItems = items.findViewById(R.id.order_totalItems_display);
            totPrice = items.findViewById(R.id.order_totalPrice_display);
            rDate = items.findViewById(R.id.order_requiredDate_display);

        }

    }
}
