package com.uee.purchaseordermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    CreateOrderAdapter createOrderAdapter;

    List<String> itemId;
    List<String> supplierID;
    List<String> name;
    List<String> description;
    List<String> measurement;
    List<Double> price;

    ArrayList<String> sitesID = new ArrayList<String>();
    ArrayList<String> sitesName = new ArrayList<String>();

    ArrayList<String> supplier_ID= new ArrayList<String>();
    ArrayList<String> supplierName = new ArrayList<String>();
    ArrayList<String> supplierAddress = new ArrayList<String>();


    Spinner sitespinner,supplierspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        recyclerView = findViewById(R.id.recyclerview_createOrder);
        sitespinner=(Spinner)findViewById(R.id.site_spinner);
        supplierspinner=(Spinner)findViewById(R.id.supplier_spinner);

        itemId = new ArrayList<>();
        supplierID = new ArrayList<>();
        name= new ArrayList<>();
        description= new ArrayList<>();
        measurement= new ArrayList<>();
        price= new ArrayList<>();



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("sites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    sitesID.add(document.getId());
                    sitesName.add(document.getString("name"));

                }

                initializeUI();

            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("suppliers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    supplier_ID.add(document.getId());
                    supplierName.add(document.getString("name"));
                    supplierAddress.add(document.getString("address"));

                }

                initializeUI();

            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        supplierspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemId.clear();
                String SUPNAME = supplier_ID.get(position);

                db.collection("items")
                        .whereEqualTo("supplierID", SUPNAME)
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                            itemId.add(document.getId());
                            supplierID.add(document.getString("supplierID"));
                            name.add(document.getString("name"));
                            description.add(document.getString("description"));
                            measurement.add(document.getString("measurement"));
                            price.add(document.getDouble("price"));
                        }
                        createOrderAdapter = new CreateOrderAdapter(CreateOrder.this,itemId,supplierID,name,description,measurement,price);
                        recyclerView.setAdapter(createOrderAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CreateOrder.this, LinearLayoutManager.VERTICAL, false));


                    } else {
                        Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initializeUI() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, sitesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sitespinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, supplierName);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplierspinner.setAdapter(dataAdapter2);

//        supplierspinner.setOnItemSelectedListener(site_listner);


    }




//    private AdapterView.OnItemSelectedListener site_listner = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            TextView tv = (TextView) view;
//            if (position == 0) {
//                // Set the hint text color gray
//                tv.setTextColor(Color.parseColor("#AAAAAA"));
//                tv.setTextSize(14);
//            }
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    } ;

}