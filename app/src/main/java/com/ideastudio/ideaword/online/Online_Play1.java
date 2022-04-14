package com.ideastudio.ideaword.online;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.model.RoomV2;
import com.ideastudio.ideaword.model.User;

import java.util.HashMap;
import java.util.Map;

public class Online_Play1 extends AppCompatActivity implements View.OnClickListener {
    private Button btnNewRoom, btnJoinRoom;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference rootDB;
    private DatabaseReference roomsTable;
ImageButton imageButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play1);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton) ;
        imageButton1.setOnClickListener(view -> {
            Intent i = new Intent(Online_Play1.this, TrangchuonlineActivity.class);
            startActivity(i);
        });

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
        if (view.getId() == btnNewRoom.getId()) {
            // tao phong moi
            processCreateRoom();

        }

        if (view.getId() == btnJoinRoom.getId()) {
            // tham gia phong moi
            startActivity(new Intent(this, Online_ThamGiaPhong.class));
        }
    }

    private void processCreateRoom() {
        progressDialog.setTitle("Đang tạo phòng mới");
        progressDialog.setMessage("Vui lòng chờ trong giây lát.");
        progressDialog.show();

        rootDB.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String userUid = mAuth.getCurrentUser().getUid();
                    for (DataSnapshot data : task.getResult().getChildren()) {
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
                } else {
                    Toast.makeText(Online_Play1.this, "Tao phong khong thanh cong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }

        });
    }


    private void createRoom(String username) {
//        String roomID, userA, userB, currentWord, currentTurn;
//        String isStart, stateGuest;
//        roomID = username;
//        userA = username;
//        userB = "";
//        currentWord = "";
//        currentTurn = username;
//        isStart = "false";
//        stateGuest = "false";
//        Room room = new Room(mAuth.getCurrentUser().getUid(), "", roomID, userA, userB, currentWord, currentTurn, isStart, stateGuest);
//
//        rootDB.child("rooms")
//                .child(roomID)
//                .setValue(room);
//        progressDialog.dismiss();
//
//        Intent intent = new Intent(this, Online_PhongCho.class);
//        intent.putExtra("roomID", username);
//        startActivity(intent);
        Map<String, Boolean> ready = new HashMap<>();
        Map<String, String> playerIDs = new HashMap<>();
        Map<String, Object> roomInfo = new HashMap<>();
        String word = "";
        String turn = username;
        ready.put(username, false);

        playerIDs.put("player1ID", mAuth.getUid());
        playerIDs.put("player1User", username);

        roomInfo.put("roomID", username);
        roomInfo.put("playerIDs", playerIDs);
//         public RoomV2(Map<String, Boolean> ready, Map<String, String> playerIDs, Map<String, Object> roomInfo, String word, String turn)
        RoomV2 room = new RoomV2(ready, roomInfo, word, turn, null);
        Map<String, Object> map = room.toMap();
        rootDB.child("rooms").child(username).setValue(map);
        progressDialog.dismiss();

        Intent intent = new Intent(this, Online_PhongCho.class);
        intent.putExtra("mRoomID", username);
        intent.putExtra("mPlayerID", mAuth.getUid());
        intent.putExtra("mPlayerUser", username);
        startActivity(intent);

    }
}