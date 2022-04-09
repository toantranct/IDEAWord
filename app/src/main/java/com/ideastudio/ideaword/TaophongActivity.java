package com.ideastudio.ideaword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ideastudio.ideaword.model.Room;

public class TaophongActivity extends AppCompatActivity {
    private Button btnCopy, btnBatDau;
    private TextView tvUserA, tvUserB, tvRoomID;
    private String roomID;

    private FirebaseAuth mAuth;
    private DatabaseReference rootDB;
    private DatabaseReference roomsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taophong);
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomID");

        mAuth = FirebaseAuth.getInstance();
        rootDB = FirebaseDatabase.getInstance().getReference();

        initView();
    }

    private void initView() {
        btnCopy = findViewById(R.id.btnCopy);
        btnBatDau = findViewById(R.id.btnBatDau);

        tvUserA = findViewById(R.id.tvUserA);
        tvUserB = findViewById(R.id.tvUserB);
        tvRoomID = findViewById(R.id.tvRoomID);
        tvRoomID.setText(roomID);
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

        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Room room = data.getValue(Room.class);
                            String uid = mAuth.getCurrentUser().getUid();
                            if (room.getUidHost().equals(uid)) {
                               tvRoomID.setText(room.getRoomID());
                               tvUserA.setText(room.getUserA());
                               Log.d("toan", tvUserB.getText().toString());
                               if (!tvUserB.getText().equals("Đang chờ đối thủ"))
                                    tvUserB.setText(room.getUserB());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void processCreateRoom() {
        
    }
}