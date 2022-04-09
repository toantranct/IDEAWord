package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {
    private EditText edtUser, edtPass;
    private Button btnLogin, btnSignUp, btnForget, btnOffline;

    private String username,password;

    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance();
        rootDb = mDatabase.getReference();


        DatabaseReference  userDB =  rootDb.child("users").child("canhcho");
        userDB.child("username").setValue("canhcho");
        userDB.child("password").setValue("123456");

        initView();
    }

    private void initView() {
        edtUser = (EditText) findViewById(R.id.username);
        edtPass = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnSignUp =(Button) findViewById(R.id.login2);
        btnForget =(Button) findViewById(R.id.login3);
        btnOffline =(Button) findViewById(R.id.login4);
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    private void processLogin() {
        username = edtUser.getText().toString();
        password = edtPass.getText().toString();
        if (isValid(username, password)) {
            // validated
            startActivity(new Intent(LoginActivity.this, TrangchuonlineActivity.class));
        } else {
            Toast.makeText(this, "Khong duoc de trong!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnLogin.getId()) {
            //xu ly login
            processLogin();
        }
    }
}