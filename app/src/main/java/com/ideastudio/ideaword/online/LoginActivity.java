package com.ideastudio.ideaword.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.model.User;
import com.ideastudio.ideaword.offline.Main2TrangchuActivity;
import com.ideastudio.ideaword.offline.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static  String Username;
    private EditText edtUser, edtPass;
    private Button btnLogin, btnSignUp, btnForget, btnOffline;

    private String username, password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;

    private SharedPreferences sharedPreferences;
    public Boolean check = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        rootDB = mDatabase.getReference();

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getUid() != null)
            startActivity(new Intent(LoginActivity.this, TrangchuonlineActivity.class));
    }

    private void initView() {
        edtUser = (EditText) findViewById(R.id.username);
        edtPass = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnForget = (Button) findViewById(R.id.login3);
        btnOffline = (Button) findViewById(R.id.btnBack);
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void processLogin() {

        username = edtUser.getText().toString();
        password = edtPass.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Khong duoc de trong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validate(username)) {
            signUpWithUserName();
        }
        else
            signUpWithEmail();
    }

    private void signUpWithUserName() {
        rootDB.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user.getUsername().equals(username)) {
                        username = user.getEmail();
                        signUpWithEmail();
                        return;
                    }
                }
                username = "aaaaaaaaaaaaaaaaaaakajsdkajsd@gmail.vn";
                signUpWithEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void signUpWithEmail() {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            Username = username;
                            startActivity(new Intent(LoginActivity.this, TrangchuonlineActivity.class));

                        } else
                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == btnLogin.getId()) {
            //
            processLogin();
        }
        if (view.getId() == btnSignUp.getId()) {
            // dang ky
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if (view.getId() == btnForget.getId()) {
            // xu ly quen mat khau
        }
        if (view.getId() == btnOffline.getId()) {
            //choi offline
            Intent intent = new Intent(this, Main2TrangchuActivity.class);
            intent.putExtra("ten", "An danh");
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        Log.d("IDEA", "onBackPressed Called");
        //
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}