package com.example.orvb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    AppCompatRadioButton rb_customer, rb_mechanic;
    TextView loginRedirect;
    AppCompatEditText signup_name, signup_email, signup_phone, signup_pwd, signup_confirm_pwd;
    AppCompatButton signup_btn;
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

        db_reference = FireBaseManager.getInstance().getDatabaseReference();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signup_name.getText().toString();
                String email = signup_email.getText().toString();
                String phone = signup_phone.getText().toString();
                String pwd = signup_pwd.getText().toString();
                String confirm_pwd = signup_confirm_pwd.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)
                        || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirm_pwd)) {
                    Toast.makeText(SignUpActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(confirm_pwd)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference userRef = db_reference.child("Userinfo").child(phone);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(SignUpActivity.this, "Phone no. is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                userRef.child("Name").setValue(name);
                                userRef.child("Email").setValue(email);
                                userRef.child("Password").setValue(pwd);
                                userRef.child("UserType").setValue(rb_value);
                                Toast.makeText(SignUpActivity.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
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
