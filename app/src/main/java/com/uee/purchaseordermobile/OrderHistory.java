package com.uee.purchaseordermobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistory extends AppCompatActivity {

    FirebaseAuth fAuth;

    String userID;

    OrderHistoryAdapter orderHistoryAdapter;

    RecyclerView recyclerView;

    //Order data
    List<String> OrderId;
    List<String> level;
    List<String> oNum;
    List<String> site;
    List<String> supplier;
    List<Double> totItems;
    List<Double> totPrice;
    List<String> rDate;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.create_order_menu){
            Intent intent = new Intent(OrderHistory.this, CreateOrder.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.log_out_menu){

            fAuth = FirebaseAuth.getInstance();
            Intent intent = new Intent(OrderHistory.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            fAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerview_orderHistory);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        //Order data
        OrderId = new ArrayList<>();
        level = new ArrayList<>();
        oNum= new ArrayList<>();
        site= new ArrayList<>();
        supplier= new ArrayList<>();
        totItems= new ArrayList<>();
        totPrice= new ArrayList<>();
        rDate= new ArrayList<>();


        db.collection("order")
                .whereEqualTo("SM_ID", userID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    List<String> itm = (List<String>) document.get("items");
                    OrderId.add(document.getId());
                    level.add(document.getString("level"));
                    oNum.add(document.getString("OrderID"));
                    site.add(document.getString("site_Name"));
                    supplier.add(document.getString("supplierName"));
                    assert itm != null;
                    totItems.add((double) itm.size());
                    totPrice.add(document.getDouble("total_price"));
                    rDate.add(document.getString("Required_date"));

                }


                orderHistoryAdapter = new OrderHistoryAdapter(OrderHistory.this,OrderId,level,oNum,site,supplier,totItems,totPrice,rDate);
                recyclerView.setAdapter(orderHistoryAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false));


            } else {
                Toast.makeText(OrderHistory.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}