package com.ideastudio.ideaword;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ideastudio.ideaword.model.Room;
import com.ideastudio.ideaword.model.User;

public class Online_Play1 extends AppCompatActivity implements View.OnClickListener{
    private Button btnNewRoom, btnJoinRoom;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference rootDB;
    private DatabaseReference roomsTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play1);

        mAuth = FirebaseAuth.getInstance();
        rootDB = FirebaseDatabase.getInstance().getReference();

        roomsTable = rootDB.child("rooms");

        progressDialog = new ProgressDialog(this);


        initView();
    }

    private void initView() {
        btnNewRoom = findViewById(R.id.btnNewRoom);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnNewRoom.getId() ) {
            // tao phong moi
            processCreateRoom();

        }

        if (view.getId() == btnJoinRoom.getId()) {
            // tham gia phong moi
            startActivity(new Intent( this, Online_ThamGiaPhong.class));
        }
    }

    private void processCreateRoom() {
        progressDialog.setTitle("Đang tạo phòng mới");
        progressDialog.setMessage("Vui lòng chờ trong giây lát.");
        progressDialog.show();
        rootDB.child("users")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String userUid = mAuth.getCurrentUser().getUid();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user.getUid() != null && user.getUid().equals(userUid)) {
                        Log.d("toan", user.getUid());
                        String username = user.getUsername();
                        createRoom(username);
                        return;
                    }
                }
                Toast.makeText(Online_Play1.this, "Tao phong khong thanh cong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createRoom(String username) {
        String roomID, userA, userB, currentWord, currentTurn;
        String isStart, stateGuest;
        roomID = username;
        userA = username;
        userB = "";
        currentWord = "";
        currentTurn = username;
        isStart = "false";
        stateGuest = "false";
        Room room = new Room(mAuth.getCurrentUser().getUid(), "", roomID, userA, userB, currentWord, currentTurn, isStart, stateGuest);

        rootDB.child("rooms")
                .child(roomID)
                .setValue(room);
        progressDialog.dismiss();

        Intent intent = new Intent(this, Online_PhongCho.class);
        intent.putExtra("roomID", username);
        startActivity(intent);

    }
}