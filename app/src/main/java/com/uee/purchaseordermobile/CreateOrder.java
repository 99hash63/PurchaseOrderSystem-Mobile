package com.uee.purchaseordermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreateOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    CreateOrderAdapter createOrderAdapter;

    //Item data
    List<String> itemId;
    List<String> supplierID;
    List<String> name;
    List<String> description;
    List<String> measurement;
    List<Double> price;

    //site data
    ArrayList<String> sitesID = new ArrayList<String>();
    ArrayList<String> sitesName = new ArrayList<String>();
    ArrayList<String> sitesAddress = new ArrayList<String>();

    //supplier data
    ArrayList<String> supplier_ID= new ArrayList<String>();
    ArrayList<String> supplierName = new ArrayList<String>();
    ArrayList<String> supplierAddress = new ArrayList<String>();


    Spinner sitespinner,supplierspinner;

    FirebaseAuth fAuth;

    String userID;

    EditText requiredDate;

    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        recyclerView = findViewById(R.id.recyclerview_createOrder);
        sitespinner=(Spinner)findViewById(R.id.site_spinner);
        supplierspinner=(Spinner)findViewById(R.id.supplier_spinner);
        requiredDate = (EditText) findViewById(R.id.requiredDate_get);

        //item data
        itemId = new ArrayList<>();
        supplierID = new ArrayList<>();
        name= new ArrayList<>();
        description= new ArrayList<>();
        measurement= new ArrayList<>();
        price= new ArrayList<>();



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getUid();


        //fetch all sites to display in the spinner
        db.collection("sites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    sitesID.add(document.getId());
                    sitesName.add(document.getString("name"));
                    sitesAddress.add(document.getString("location"));

                }



            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //fetch all suppliers to display in the spinner
        db.collection("suppliers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    supplier_ID.add(document.getId());
                    supplierName.add(document.getString("name"));
                    supplierAddress.add(document.getString("address"));

                }

                initializeSpinnerData();

            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //change item list when user select an supplier and display data according to the supplier
        supplierspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //clear the item list
                itemId.clear();

                //get the selected item position
                String SUPNAME = supplier_ID.get(position);

                //fetch items according to the selected supplier and display
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


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        
        requiredDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateOrder.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void initializeSpinnerData() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, sitesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sitespinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, supplierName);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplierspinner.setAdapter(dataAdapter2);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        requiredDate.setText(sdf.format(myCalendar.getTime()));
    }

}