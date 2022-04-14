package com.ideastudio.ideaword.online;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.model.RoomV2;
import com.ideastudio.ideaword.model.Utils;

import java.util.List;
import java.util.Map;

public class Online_PhongCho extends AppCompatActivity {
    private Button btnCopy, btnBatDau, btnSanSang;
    private TextView tvUserA, tvUserB, tvRoomID;
    private String mRoomID, mPlayerID, mPlayerUser;

    private FirebaseAuth mAuth;
    private DatabaseReference rootDB;

    private List<Dict> dicts;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongcho);
        Intent intent = getIntent();
        mRoomID = intent.getStringExtra("mRoomID");
        mPlayerID = intent.getStringExtra("mPlayerID");
        mPlayerUser = intent.getStringExtra("mPlayerUser");

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
        tvRoomID.setText(mRoomID);


        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String uid = mAuth.getUid();
                            RoomV2 room = data.getValue(RoomV2.class);

                            Map<String, Boolean> ready = room.getReady();
                            String player1User = room.getPlayer1User();
                            String player2User = room.getPlayer2User();
                            String player1ID = room.getPlayer1ID();
                            String player2ID = room.getPlayer2ID();
                            Boolean readyPlayer2 = ready.get(player2User);
                            Boolean readyPlayer1 = ready.get(player1User);
                            if (readyPlayer2 == null) readyPlayer2 = false;

                            if (mRoomID.equals(room.getRoomID())) {
                                if (player1ID.equals(uid)) {
                                    tvRoomID.setText(mRoomID);
                                    tvUserA.setText(room.getPlayer1User());
                                    if (player2User != null) {
                                        tvUserB.setText(player2User);
                                        if (!readyPlayer2)
                                            btnBatDau.setEnabled(false);
                                        else
                                            btnBatDau.setEnabled(true);
                                    }
                                } else if (player2ID.equals(uid)) {
                                    tvUserB.setText(player2User);
                                    tvRoomID.setText(mRoomID);
                                    tvUserA.setText(player1User);
                                    btnSanSang.setVisibility(View.VISIBLE);
                                    if (readyPlayer1 && readyPlayer2) processCreateRoom();
                                }
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
                String stringClipBoard = tvRoomID.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", stringClipBoard);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Online_PhongCho.this, "Đã sao chép ID vào bộ nhớ tạm!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSanSang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootDB.child("rooms").child(mRoomID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            RoomV2 room = task.getResult().getValue(RoomV2.class);
                            Map<String, Boolean> ready = room.getReady();
                            String player2User = room.getPlayer2User();
                            Boolean readyPlayer2 = ready.get(player2User);
                            if (readyPlayer2 == null) readyPlayer2 = false;
                            Log.d("toan", readyPlayer2.toString());
                            readyPlayer2 = !readyPlayer2;
                            ready.replace(player2User, readyPlayer2);
                            Log.d("toan", readyPlayer2.toString());
                            String mess = "Sẵn sàng";
                            if (readyPlayer2) mess = "Huỷ sẵn sàng";
                            btnSanSang.setText(mess);
                            room.setReady(ready);
                            rootDB.child("rooms")
                                    .child(room.getRoomID())
                                    .setValue(room.toMap());
                        }
                        else {
                            Toast.makeText(Online_PhongCho.this, "Có lỗi khi sẵn sàng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cap nhat trang thai san sang cua host
                rootDB.child("rooms").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot data : task.getResult().getChildren()) {
                                RoomV2 room = data.getValue(RoomV2.class);
                                Map<String, Boolean> ready = room.getReady();
                                String player1User = room.getPlayer1User();

                                ready.put(player1User, true);
                                room.setReady(ready);

                                rootDB.child("rooms")
                                        .child(room.getRoomID())
                                        .setValue(room.toMap());

                                processCreateRoom();

                            }
                        } else {
                            Toast.makeText(Online_PhongCho.this, "Có lỗi khi sẵn sàng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }

    private void processCreateRoom() {
        Intent intent = new Intent(this, Online_Play2.class);
        intent.putExtra("mRoomID", mRoomID);
        intent.putExtra("mPlayerID", mAuth.getUid());
        intent.putExtra("mPlayerUser", mPlayerUser);
        startActivity(intent);
    }

    private void resetHandler() {
        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}

