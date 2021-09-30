package com.uee.purchaseordermobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Receipt extends AppCompatActivity {

    String orderID;

    FirebaseAuth fAuth;

    RecyclerView recyclerView;

    ReceiptAdapter receiptAdapter;

    List<String> itemId;
    List<String> name;
    List<Double> quantity;
    List<Double> price;

    TextView status, orderNum, siteName, siteAddress, supplierName, supplierAddress, createdData, requiredDate,totalPrice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.create_order_menu){
            Intent intent = new Intent(Receipt.this, CreateOrder.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.order_history_menu){
            Intent intent = new Intent(Receipt.this, OrderHistory.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.log_out_menu){

            fAuth = FirebaseAuth.getInstance();
            Intent intent = new Intent(Receipt.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            fAuth.signOut();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        recyclerView=findViewById(R.id.table_recyclerView);

        status=findViewById(R.id.order_status_level);
        orderNum=findViewById(R.id.order_status_number);
        siteName=findViewById(R.id.order_status_siteName);
        siteAddress=findViewById(R.id.order_status_siteAddress);
        supplierName=findViewById(R.id.order_status_supplierName);
        supplierAddress=findViewById(R.id.order_status_supplierAddress);
        requiredDate=findViewById(R.id.order_status_requiredDate);
        createdData=findViewById(R.id.order_status_createdDate);
        totalPrice=findViewById(R.id.order_status_totPrice);


        orderID = getIntent().getExtras().getString("orderID");


        itemId = new ArrayList<>();
        name = new ArrayList<>();
        quantity= new ArrayList<>();
        price= new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    assert document != null;

                    if (document.exists()) {

                        status.setText(document.getString("level"));
                        orderNum.setText("Order No: #"+document.getString("OrderID"));
                        siteName.setText("Site Name :- "+document.getString("site_Name"));
                        siteAddress.setText("Site Address :- "+document.getString("site_address"));
                        supplierName.setText("Suplier Name :- "+document.getString("supplierName"));
                        supplierAddress.setText("Supplier Address :- "+document.getString("supplier_address"));
                        createdData.setText("Created Date :- "+document.getString("Purchase_date"));
                        requiredDate.setText("Required Date :- "+document.getString("Required_date"));
                        totalPrice.setText("Total Price :- LKR"+document.get("total_price").toString());


                        List<Map> itm = (List<Map>) document.get("items");

                       for (int i=0; i<itm.size();i++){

                           itemId.add(Objects.requireNonNull(itm.get(i).get("item_id")).toString());
                           name.add(Objects.requireNonNull(itm.get(i).get("item_name")).toString());
                           price.add(Double.valueOf(itm.get(i).get("price").toString()));
                           quantity.add(Double.valueOf(itm.get(i).get("quantity").toString()));


                       }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Receipt.this));
                        receiptAdapter = new ReceiptAdapter(Receipt.this,itemId,name,quantity,price);
                        recyclerView.setAdapter(receiptAdapter);

                        if (status.getText().equals("Pending Approval")){
                            status.setTextColor(getResources().getColor(R.color.light_black));
                        }else if (status.getText().equals("Approved")) {
                            status.setTextColor(getResources().getColor(R.color.green));
                        }else if (status.getText().equals("Declined")){
                            status.setTextColor(getResources().getColor(R.color.pink_dark_red));
                        }else if (status.getText().equals("Partially Approved")){
                            status.setTextColor(getResources().getColor(R.color.green));
                        }else {
                            status.setTextColor(getResources().getColor(R.color.yellow));

                        }


                    } else {

                    }
                } else {

                }
            }
        });







    }
}