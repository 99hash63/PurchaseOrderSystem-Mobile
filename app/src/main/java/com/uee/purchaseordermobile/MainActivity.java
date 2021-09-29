package com.uee.purchaseordermobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button login;
    EditText email,password;
    TextView forgetpass;
    String userID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.log_email_txt);
        password = findViewById(R.id.log_password_txt);
        login = findViewById(R.id.loginBtn);
        forgetpass = findViewById(R.id.forgetpass);

        fAuth = FirebaseAuth.getInstance();

        //check if user is already logged in
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), CreateOrder.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(mpassword)){
                    password.setError("Password is required");
                    return;
                }

                if(mpassword.length() < 5){
                    password.setError("Password must greater than 4 characters");
                }

                //authenticate user
                fAuth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), CreateOrder.class));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email To receive reset link");
                passwordResetDialog.setView(resetmail);

                passwordResetDialog.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send email
                        String mail = resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error! Link is not sent", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.create().show();
            }
        });


    }
}