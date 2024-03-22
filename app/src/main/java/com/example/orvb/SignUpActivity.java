package com.example.orvb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    AppCompatRadioButton rb_customer, rb_mechanic;
    TextView loginRedirect;
    AppCompatEditText signup_name, signup_email, signup_phone, signup_pwd, signup_confirm_pwd;
    AppCompatButton signup_btn;
    FirebaseDatabase database;
    DatabaseReference db_reference;

    int rb_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_phone = findViewById(R.id.signup_phone);
        signup_pwd = findViewById(R.id.signup_password);
        signup_confirm_pwd = findViewById(R.id.signup_passwordconfirm);
        signup_btn = findViewById(R.id.signup_btn);
        rb_customer = findViewById(R.id.radio_customer);
        rb_mechanic = findViewById(R.id.radio_mechanic);
        loginRedirect = findViewById(R.id.loginRedirect);

        if (rb_customer.isChecked()){
            rb_value = 1;
        }

        rb_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_customer.setTextColor(Color.WHITE);
                rb_mechanic.setTextColor(Color.BLACK);
                rb_value = 1;
            }
        });

        rb_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_mechanic.setTextColor(Color.WHITE);
                rb_customer.setTextColor(Color.BLACK);
                rb_value = 0;
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                db_reference = database.getReferenceFromUrl("https://orvb-sem-proj-default-rtdb.firebaseio.com/");

                String name = Objects.requireNonNull(signup_name.getText()).toString();
                String email = Objects.requireNonNull(signup_email.getText()).toString();
                String phone = Objects.requireNonNull(signup_phone.getText()).toString();
                String pwd = Objects.requireNonNull(signup_pwd.getText()).toString();
                String confirm_pwd = Objects.requireNonNull(signup_confirm_pwd.getText()).toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pwd.isEmpty() || confirm_pwd.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(confirm_pwd)) {
                    // Passwords don't match, show a toast
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Passwords match
                    db_reference.child("Userinfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // User account exists or not
                            if (snapshot.hasChild(phone)){
                                Toast.makeText(SignUpActivity.this, "Phone no. is already registered", Toast.LENGTH_SHORT).show();
                            } else {

                                db_reference.child("Userinfo").child(phone).child("Name").setValue(name);
                                db_reference.child("Userinfo").child(phone).child("Email").setValue(email);
                                db_reference.child("Userinfo").child(phone).child("Password").setValue(pwd);
                                db_reference.child("Userinfo").child(phone).child("UserType").setValue(rb_value);

                                Toast.makeText(SignUpActivity.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

}