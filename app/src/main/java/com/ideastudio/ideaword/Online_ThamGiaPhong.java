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

public class Online_ThamGiaPhong extends AppCompatActivity {
    private Button btnJoinRoom;
    private EditText edtRoomID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;
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
                processJoinRoom();
            }
        });
    }

    private void processJoinRoom() {
        Log.d("toan", "test");
        rootDB.child("rooms")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Room room = data.getValue(Room.class);
                            if (room.getRoomID().equals(roomID)) {
                                Room updateRoom = new Room(room.getUidHost(), roomID, room.getUserA(), mAuth.getCurrentUser().getUid(), room.getCurrentWord(), room.getCurrentTurn());
                                rootDB.child("rooms")
                                        .child(roomID)
                                        .setValue(updateRoom);
                                startActivity(new Intent(Online_ThamGiaPhong.this, ThamgiaphongActivity.class));
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

