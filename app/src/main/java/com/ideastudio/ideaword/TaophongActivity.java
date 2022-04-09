package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaophongActivity extends AppCompatActivity {
    private Button btnCopy, btnBatDau;
    private TextView tvUserA, tvUserB, tvRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taophong);

        initView();
    }

    private void initView() {
        btnCopy = findViewById(R.id.btnCopy);
        btnBatDau = findViewById(R.id.btnBatDau);

        tvUserA = findViewById(R.id.tvUserA);
        tvUserB = findViewById(R.id.tvUserB);
        tvRoomID = findViewById(R.id.tvRoomID);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // copy
                String stringClipBorad = tvRoomID.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", stringClipBorad);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(TaophongActivity.this, "Đã sao chép ID vào bộ nhớ tạm!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // btn bat dau
                processCreateRoom();

            }
        });
    }

    private void processCreateRoom() {
        
    }
}