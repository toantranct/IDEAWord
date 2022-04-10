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

public class Online_PhongCho extends AppCompatActivity {
    private Button btnCopy, btnBatDau, btnSanSang;
    private TextView tvUserA, tvUserB, tvRoomID;
    private String roomID;

    private FirebaseAuth mAuth;
    private DatabaseReference rootDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongcho);
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomID");

        mAuth = FirebaseAuth.getInstance();
        rootDB = FirebaseDatabase.getInstance().getReference();

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        btnCopy = findViewById(R.id.btnCopy);
        btnBatDau = findViewById(R.id.btnBatDau);
        btnSanSang = findViewById(R.id.btnSanSang);

        tvUserA = findViewById(R.id.tvUserA);
        tvUserB = findViewById(R.id.tvUserB);
        tvRoomID = findViewById(R.id.tvRoomID);
        tvRoomID.setText(roomID);

        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Room room = data.getValue(Room.class);
                            String uid = mAuth.getUid();

                            if (room.getUidHost().equals(uid)) {
                                tvRoomID.setText(room.getRoomID());
                                tvUserA.setText(room.getUserA());
                                if (room.getUserB() != "") {
                                    tvUserB.setText(room.getUserB());
                                    btnBatDau.setEnabled(true);
                                }

                            }

                            if (room.getUidGuest().equals(uid)) {
                                tvUserB.setText(room.getUserB());
                                tvRoomID.setText(room.getRoomID());
                                tvUserA.setText(room.getUserA());
                                btnSanSang.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // copy
                String stringClipBorad = tvRoomID.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", stringClipBorad);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Online_PhongCho.this, "Đã sao chép ID vào bộ nhớ tạm!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSanSang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootDB.child("rooms")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Room room = data.getValue(Room.class);
                                    if (room.getUidGuest().equals(mAuth.getUid())) {
                                        String stateGuest =  room.getStateGuest().equals("false") ? "true" : "false";
                                        room.setStateGuest(stateGuest);
                                        String text = stateGuest.equals("true") ? "Huỷ sẵn sàng" : "Sẵn sàng";
                                        btnSanSang.setText(text);
                                        rootDB.child("rooms")
                                                .child(room.getUserA())
                                                .setValue(room);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootDB.child("rooms")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Room room = data.getValue(Room.class);
                                    if (room.getUidHost().equals(mAuth.getUid())) {
                                        if (room.getStateGuest().equals("false")) {
                                            String message = room.getUserB() + " chưa sẵn sàng";
                                            Toast.makeText(Online_PhongCho.this, message, Toast.LENGTH_SHORT).show();
                                        } else
                                            if (room.getIsStart().equals("false")) {
                                            room.setIsStart("true");
                                            rootDB.child("rooms")
                                                    .child(room.getUserA())
                                                    .setValue(room);
                                            processCreateRoom();
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });


    }

    private void processCreateRoom() {
        startActivity(new Intent(this, Online_Play2.class));
    }
}