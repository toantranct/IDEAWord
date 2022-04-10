package com.ideastudio.ideaword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ideastudio.ideaword.model.Room;
import com.ideastudio.ideaword.model.User;

public class Online_ThamGiaPhong extends AppCompatActivity {
    private Button btnJoinRoom;
    private EditText edtRoomID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;

    private String username;
    private String roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_tham_gia_phong);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        rootDB = mDatabase.getReference();

        initView();
    }

    private void initView() {
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        edtRoomID = findViewById(R.id.edtRoomID);

        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomID = edtRoomID.getText().toString();
                getUserName();
            }
        });
    }


    private void getUserName() {
        rootDB.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user.getUid().equals(mAuth.getUid())) {
                        username = user.getUsername();
                        processJoinRoom(username);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void processJoinRoom(String username) {
        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Room room = data.getValue(Room.class);
                            if (room.getRoomID().equals(roomID)) {
                                Room updateRoom = new Room(room.getUidHost(), mAuth.getUid(), roomID, room.getUserA(), username, room.getCurrentWord(), room.getCurrentTurn(), room.getIsStart(), room.getStateGuest());
                                rootDB.child("rooms")
                                        .child(roomID)
                                        .setValue(updateRoom);
                                startActivity(new Intent(Online_ThamGiaPhong.this, Online_PhongCho.class));
                                return;
                            }
                        }
                        Toast.makeText(Online_ThamGiaPhong.this, "ID khong ton tai", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

