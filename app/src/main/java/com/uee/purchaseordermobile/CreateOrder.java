package com.uee.purchaseordermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    CreateOrderAdapter createOrderAdapter;

    List<String> id;
    List<String> supplierID;
    List<String> name;
    List<String> description;
    List<String> measurement;
    List<Double> price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        recyclerView = findViewById(R.id.recyclerview_createOrder);

        id = new ArrayList<>();
        supplierID = new ArrayList<>();
        name= new ArrayList<>();
        description= new ArrayList<>();
        measurement= new ArrayList<>();
        price= new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    id.add(document.getId());
                    supplierID.add(document.getString("supplierID"));
                    name.add(document.getString("name"));
                    description.add(document.getString("description"));
                    measurement.add(document.getString("measurement"));
                    price.add(document.getDouble("price"));
                }

                createOrderAdapter = new CreateOrderAdapter(this,id,supplierID,name,description,measurement,price);
                recyclerView.setAdapter(createOrderAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });





    }

}