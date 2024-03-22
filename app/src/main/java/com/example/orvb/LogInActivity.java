package com.example.orvb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    AppCompatEditText login_phone, login_pwd;
    AppCompatButton login_btn;
    TextView signupRedirect;
    DatabaseReference db_reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        login_phone = findViewById(R.id.login_phone);
        login_pwd = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        signupRedirect = findViewById(R.id.SignUpRedirect);

        db_reference = FireBaseManager.getInstance().getDatabaseReference().child("Userinfo");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() & validatePassword()) {
                    checkEmail();
                }
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });
    }


    public Boolean validateEmail() {
        String val = login_phone.getText().toString();
        if (val.isEmpty()) {
            login_phone.setError("Enter Phone no.");
            return false;
        } else {
            login_phone.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = login_pwd.getText().toString();
        if (val.isEmpty()) {
            login_pwd.setError("Enter Password");
            return false;
        } else {
            login_pwd.setError(null);
            return true;
        }
    }

    public void checkEmail() {
        String phone = login_phone.getText().toString();
        String pwd = login_pwd.getText().toString();

        db_reference.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String getPwd = snapshot.child("Password").getValue(String.class);
                    if (getPwd != null && getPwd.equals(pwd)) {
                        Toast.makeText(LogInActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        UserManager.getInstance().setPhoneNumber(phone);

                        int userType = snapshot.child("UserType").getValue(Integer.class);
                        Class<?> destinationActivity = userType == 1 ? MainActivity.class : MechanicMainActivity.class;
                        startActivity(new Intent(LogInActivity.this, destinationActivity));
                    } else {
                        Toast.makeText(LogInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
