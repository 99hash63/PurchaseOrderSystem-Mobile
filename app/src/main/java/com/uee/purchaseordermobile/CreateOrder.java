package com.uee.purchaseordermobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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


    Spinner siteSpinner, supplierSpinner;

    FirebaseAuth fAuth;

    String userID,uniqueId;

    EditText requiredDate, comments;

    Button release;

    TextView orderNoDisplay;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.order_history_menu){
            Intent intent = new Intent(CreateOrder.this, OrderHistory.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.log_out_menu){

            fAuth = FirebaseAuth.getInstance();
            Intent intent = new Intent(CreateOrder.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            fAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    final Calendar myCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        recyclerView = findViewById(R.id.recyclerview_createOrder);
        siteSpinner =(Spinner)findViewById(R.id.site_spinner);
        supplierSpinner =(Spinner)findViewById(R.id.supplier_spinner);
        requiredDate = (EditText) findViewById(R.id.requiredDate_get);
        comments = (EditText) findViewById(R.id.comment_get);
        release = findViewById(R.id.releaseOrder_btn);
        orderNoDisplay = findViewById(R.id.orderNoDisplay);


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

        if(uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();

            orderNoDisplay.setText("Order No: #"+uniqueId.substring(1,8) );
        }


        //fetch all sites to display in the spinner
        db.collection("sites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    sitesID.add(document.getId());
                    sitesName.add(document.getString("name"));
                    sitesAddress.add(document.getString("location"));
                    initializeSpinnerData();
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

                createOrderAdapter = new CreateOrderAdapter(CreateOrder.this,itemId,supplierID,name,description,measurement,price);
                recyclerView.setAdapter(createOrderAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(CreateOrder.this, LinearLayoutManager.VERTICAL, false));

            } else {
                Toast.makeText(CreateOrder.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //change item list when user select an supplier and display data according to the supplier
        supplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //get the selected item position
                String SUPNAME = supplier_ID.get(position);

                //fetch items according to the selected supplier and display
                db.collection("items")
                        .whereEqualTo("supplierID", SUPNAME)
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //clear the item list
                        itemId.clear();
                        supplierID.clear();
                        name.clear();
                        description.clear();
                        measurement.clear();
                        price.clear();

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

        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double totalPrice = 0;

                ArrayList<Map> docArray = new ArrayList<>();



                for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {

                    View view = recyclerView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.itemCard_checkBox);
                    EditText quantity = (EditText) view.findViewById(R.id.itemCard_input_quantity);
                    int quantityValue = 0;
                    if (!quantity.getText().toString().isEmpty()){
                        try {
                            quantityValue = Integer.parseInt(quantity.getText().toString());
                        }catch (NumberFormatException e){
                            Log.e("NumberFormatException",e.getMessage());
                        }

                    }



                    if(checkBox.isChecked()){

                        Map<String, Object> docMap = new HashMap<>();
                        docMap.put("item_id",itemId.get(i));
                        docMap.put("item_name",name.get(i));
                        docMap.put("price",price.get(i));
                        docMap.put("quantity",quantityValue);
                        docArray.add(docMap);

                        totalPrice = totalPrice + price.get(i) * quantityValue;

                    }

                }
                //get site and supplier array position
                int selectedSitePosition = siteSpinner.getSelectedItemPosition();
                int selectedSupplierPosition = supplierSpinner.getSelectedItemPosition();

                //get current date
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                //preparing order data to be pass to Database
                Map<String, Object> OrderData = new HashMap<>();
                OrderData.put("SM_ID", userID);
                OrderData.put("OrderID", uniqueId.substring(1,8));

                OrderData.put("site_ID", sitesID.get(selectedSitePosition));
                OrderData.put("site_Name", sitesName.get(selectedSitePosition));
                OrderData.put("site_address", sitesAddress.get(selectedSitePosition));

                OrderData.put("supplierID", supplier_ID.get(selectedSupplierPosition));
                OrderData.put("supplierName", supplierName.get(selectedSupplierPosition));
                OrderData.put("supplier_address", supplierAddress.get(selectedSupplierPosition));

                OrderData.put("Purchase_date", formattedDate);
                OrderData.put("Required_date", requiredDate.getText().toString());

                OrderData.put("order_status", "Pending Approval");
                OrderData.put("level", "Pending Approval");

                OrderData.put("total_price", totalPrice);

                OrderData.put("comments", comments.getText().toString());

                OrderData.put("items",docArray);


                db.collection("order")
                        .add(OrderData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(CreateOrder.this, "Order Created Successfully", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateOrder.this, "Order Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });




    }

    private void initializeSpinnerData() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, sitesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        siteSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter( CreateOrder.this, android.R.layout.simple_spinner_dropdown_item, supplierName);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplierSpinner.setAdapter(dataAdapter2);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        requiredDate.setText(sdf.format(myCalendar.getTime()));
    }

}