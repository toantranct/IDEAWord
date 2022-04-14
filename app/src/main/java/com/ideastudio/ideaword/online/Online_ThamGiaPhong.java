package com.ideastudio.ideaword.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Map;

public class Online_ThamGiaPhong extends AppCompatActivity {
    private Button btnJoinRoom;
    private EditText edtRoomID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;

    private String username;
    private String roomID;
ImageButton imageButton1,imageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_tham_gia_phong);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton1) ;
        imageButton2=(ImageButton)findViewById(R.id.imagebutton2) ;
        imageButton1.setOnClickListener(view -> {
             Intent i = new Intent(Online_ThamGiaPhong.this, Online_Play1.class);
            startActivity(i);
        });
        imageButton2.setOnClickListener(view -> {
            Intent i = new Intent(Online_ThamGiaPhong.this,TrangchuonlineActivity.class);
            startActivity(i);
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        rootDB = mDatabase.getReference();

        rootDB.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot data : task.getResult().getChildren()) {
                        User user = data.getValue(User.class);
                        if (user.getUid().equals(mAuth.getUid())) {
                            username = user.getUsername();
                            initView();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(Online_ThamGiaPhong.this, "Đã có lỗi xảy ra, thử lại sau", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

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

    public static <T> T getNestedValue(Map map, String... keys) {
        Object value = map;

        for (String key : keys) {
            value = ((Map) value).get(key);
        }

        return (T) value;
    }

    private void processJoinRoom() {
        rootDB.child("rooms").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot data : task.getResult().getChildren()) {
                        String uid = mAuth.getUid();
                        RoomV2 room = data.getValue(RoomV2.class);
                        Map<String, Object> roomInfo = room.getRoomInfo();
                        Map<String, String> playerIDs = (Map<String, String>) roomInfo.get("playerIDs");
                        Map<String, Boolean> ready = room.getReady();
                        if (roomID.equals(room.getRoomID())) {
                            // tim thay phong
                            playerIDs.put("player2ID", uid);
                            playerIDs.put("player2User", username);
                            ready.put(username, false);

                            RoomV2 updateRoom = room;
                            updateRoom.setPlayerIDs(playerIDs);
                            updateRoom.setReady(ready);
                            rootDB.child("rooms")
                                    .child(roomID)
                                    .setValue(updateRoom.toMap());
                            Intent intent = new Intent(Online_ThamGiaPhong.this, Online_PhongCho.class);
                            intent.putExtra("mRoomID", roomID);
                            intent.putExtra("mPlayerID", uid);
                            intent.putExtra("mPlayerUser", username);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(Online_ThamGiaPhong.this, "ID không tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Online_ThamGiaPhong.this, "Có lỗi vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("IDEA", "onBackPressed Called");
        startActivity(new Intent(this, Online_Play1.class));
        finish();
    }
}

