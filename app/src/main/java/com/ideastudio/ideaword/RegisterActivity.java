package com.ideastudio.ideaword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ideastudio.ideaword.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button btnSignUp, btnBack;
    private EditText edtUsername, edtPassword, edtRepass, edtEmail;
    private CheckBox checkBox;

    private String username, password, repass, email;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        rootDB = mDatabase.getReference();
        initView();
    }

    private void initView() {
        btnSignUp = findViewById(R.id.btnSignUp);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        edtRepass = findViewById(R.id.password2);
        edtEmail = findViewById(R.id.email);
        checkBox = findViewById(R.id.checkBox);
        btnBack = findViewById(R.id.btnBack);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processSignUp();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void processSignUp() {
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        repass = edtRepass.getText().toString();
        email = edtEmail.getText().toString();

        if (isValid(username, password, repass, email)) {
            Log.d("toan", "validted");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                User user = new User(mAuth.getUid(), username, password, email);
                                handlerUserInformation(user);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký không thành công, thử lại sau!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void handlerUserInformation(User user) {
        rootDB
                .child("users")
                .child(user.getUid())
                .setValue(user);
    }

    private boolean isValid(String username, String password, String repass, String email) {
        String mess = null;
        if (username.isEmpty() || password.isEmpty() || repass.isEmpty() || email.isEmpty()) {
            mess = "Cac truong khong duoc de trong!";
        } else if (!password.equals(repass)) {
            mess = "Mat khau khong khop!";
        }
        if (password.length() < 6) {
            mess = "Mật khẩu tổi thiếu 6 ký tự";
        } else if (!validate(email))
            mess = "Email khong dung dinh dang!";
        Log.d("toan", String.valueOf(validate(email)));
        if (mess == null)
            return true;
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
        return false;
    }
}