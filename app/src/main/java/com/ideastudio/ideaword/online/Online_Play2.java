package com.ideastudio.ideaword.online;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.model.RoomV2;
import com.ideastudio.ideaword.model.Utils;
import com.ideastudio.ideaword.offline.Main2TrangchuActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Online_Play2 extends AppCompatActivity {
    ImageButton imageButton;
    private static int c = 1;
    private Button btnCheck;
    private TextView currentWord;
    private EditText playerWord;
    private TextView prefixPlayer;
    private TextView countDownView;
    private TextView roundView;
    private TextView currentTurn;
    private Utils utils;
    private CountDownTimer countDownTimer;
    private boolean stateDialog = false;
    // thay doi theo De hoac kho
    private int level = 21;
    private int currentRound = 1;

    private String currentPlayerWord = "";
    private boolean isStart = true;
    private List<Dict> dicts;
    List<String> availableWord = new ArrayList<>();
    private String mRoomID, mPlayerID, mPlayerUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference rootDB;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        rootDB = mDatabase.getReference();
        Intent intent = getIntent();
        mRoomID = intent.getStringExtra("mRoomID");
        mPlayerID = intent.getStringExtra("mPlayerID");
        mPlayerUser = intent.getStringExtra("mPlayerUser");

        Gson gson = new Gson();
        String jsonFileString = utils.getJsonFromAssets(getApplicationContext(), "words.json");
        Type listUserType = new TypeToken<List<Dict>>() {
        }.getType();
        dicts = gson.fromJson(jsonFileString, listUserType);

        rootDB.child("rooms")
                .child(mRoomID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RoomV2 room = snapshot.getValue(RoomV2.class);
                        String winUser = room.getWinUser();
                        if (!winUser.equals("")) {
                            Log.d("Test", "update win " + mRoomID);
                            if (stateDialog == false) {
                                showAlertLose(winUser);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
    }

    private void setRound(int round) {
        roundView.setText(String.valueOf(round));
    }

    private void initCountDown(int level, String player1, String player2) {
        if (countDownTimer == null)
            countDownTimer = new CountDownTimer(level * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    countDownView.setText(String.valueOf(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    //countDownView.setText("done!");
                    if (stateDialog == false) {
                        String winUser;
                        winUser = player1;
                        if (winUser.equals(currentTurn.getText().toString()))
                            winUser = player2;
                        showAlertLose(winUser);
                    }

                }
            };
        countDownTimer.cancel();
        countDownTimer.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initView() throws IOException {
        if (isStart) {
            playerWord = findViewById(R.id.playerWord);
            currentWord = findViewById(R.id.currentWord);
            prefixPlayer = findViewById(R.id.prefixPlayer);
            roundView = findViewById(R.id.textView5_2);
            countDownView = findViewById(R.id.textView5_3);
            currentTurn = findViewById(R.id.tvCurrentUser);
            btnCheck = findViewById(R.id.btnCheck);
            imageButton = (ImageButton) findViewById(R.id.imagebutton);

            roundView.setText("");
            imageButton.setOnClickListener(view -> {
                Intent i = new Intent(Online_Play2.this, TrangchuonlineActivity.class);
                startActivity(i);
                finish();
            });
         //   currentRound = 1;
            isStart = false;
        }
        //setRound(currentRound);
        if (isPlayer1()) genWord();
        getWord();
    }

    public void genWord() {
        // random word

        Random generator = new Random();
        int randomIndex = generator.nextInt(dicts.size() - 1);

        String randomWord = dicts.get(randomIndex).getWord();
        currentWord.setText(randomWord);

        // update word
        rootDB.child("rooms").child(mRoomID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomV2 room = task.getResult().getValue(RoomV2.class);
                    room.setWord(currentWord.getText().toString());
                    room.setTurn(room.getPlayer1User());
                    rootDB.child("rooms")
                            .child(mRoomID)
                            .setValue(room.toMap());
                } else {
                    Toast.makeText(Online_Play2.this, "Có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void getWord() {
        // lay word hien tai tu server cho player
        rootDB.child("rooms").child(mRoomID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomV2 room = snapshot.getValue(RoomV2.class);
                currentWord.setText(room.getWord());
                String[] count = currentWord.getText().toString().split("\\s+");
                if (count.length != 2) return;
                String turn = room.getTurn();
                if (turn == "") turn = room.getPlayer1User();

                currentTurn.setText(turn);
                prefixPlayer.setText(count[1]);
                playerWord.setText("");

                initCountDown(level, room.getPlayer1User(), room.getPlayer2User());

                if (mPlayerUser.equals(room.getTurn()))
                    btnCheck.setVisibility(View.VISIBLE);
                else
                    btnCheck.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean isPlayer1() {
        return mPlayerUser.equals(mRoomID);
    }

    private boolean isPlayer2() {
        return !mPlayerUser.equals(mRoomID);
    }

    private String convertToUnsignedString(String word) {
        // chuyển về không dấu
        // if word have "đ" use "đ".Normalize(NormalizationForm.FormD) -- use loop process
        word = word.replaceAll("đ", "d");
        String dest = Normalizer.normalize(word, Normalizer.Form.NFD);
        dest = dest.replaceAll("[^\\p{ASCII}]", "");

        return dest;
    }

    private void checkPlayerWord(String firstWord) {
        // tao mang gom cac tu co the tao
        availableWord.clear();

        String firstWordStock = prefixPlayer.getText().toString();

        if (dicts != null || !dicts.isEmpty()) {
            for (Dict dict : dicts) {
                // kiem tra co tu can tim ko
                currentPlayerWord = firstWordStock + " " + playerWord.getText().toString().trim();
                if (dict.getWord().toLowerCase(Locale.ROOT).equals(currentPlayerWord.toLowerCase(Locale.ROOT))) {
                    availableWord.add(dict.getWord());
                    break;
                }
            }
        }

        if (availableWord.isEmpty()) {
            // Win
            updateWinner();
            Log.d("IDEA_CHECKWINER_LOG", mPlayerID);
        } else {
            Toast.makeText(Online_Play2.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
            // tiep tuc

            swapTurn(currentPlayerWord);
        }

    }

    private void updateWinner() {
        rootDB.child("rooms").child(mRoomID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomV2 room = task.getResult().getValue(RoomV2.class);
                    String winUser = currentTurn.getText().toString();
                    Map<String, Boolean> ready = room.getReady();
                    for (String player : ready.keySet()) {
                        if (!winUser.equals(player)) {
                            winUser = player;
                            break;
                        }
                    }
                    room.setWinUser(winUser);
                    rootDB.child("rooms")
                            .child(mRoomID)
                            .setValue(room.toMap());
                }
            }
        });
    }

    private void swapTurn(String word) {
        rootDB.child("rooms").child(mRoomID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomV2 room = task.getResult().getValue(RoomV2.class);
                    String turn = room.getTurn();
                    Map<String, Boolean> ready = room.getReady();
                    for (String playerUser : ready.keySet()) {
                        if (!turn.equals(playerUser)) {
                            turn = playerUser;
                            break;
                        }
                    }
                    room.setTurn(turn);
                    room.setWord(word);
                    rootDB.child("rooms")
                            .child(mRoomID)
                            .setValue(room.toMap());

                }


            }
        });
    }


    public void BtnCheckClick(View view) {

        if (playerWord.getText().toString().trim().isEmpty()) {
            Toast.makeText(Online_Play2.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
        } else {
            String[] pcWord = currentWord.getText().toString().split(" ");
            checkPlayerWord(convertToUnsignedString(pcWord[1]));
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showAlertLose(String winUser) {
        String title;
        if (!winUser.equals(mPlayerUser))
            title = "Bạn đã thua";
        else
            title = "Chúc mừng bạn đã thắng";

        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Trò chơi kết thúc!");
        alert.setMessage(title);
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Trang chủ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                Intent intent = new Intent(Online_Play2.this, TrangchuonlineActivity.class);
                startActivity(intent);
                //leaveRoom();
                finish();
            }
        });
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Chơi lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                rePlayGame();
            }
        });
        if (!((Activity) this).isFinishing()) {
            stateDialog = true;
            alert.show();
        }

    }

    public void showAlertRoomNotExist() {
        String title;
        title = "Chủ phòng đã thoát";
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Thoát khỏi phòng");
        alert.setMessage(title);
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Trang chủ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                Intent intent = new Intent(Online_Play2.this, TrangchuonlineActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Tạo phòng mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                startActivity(new Intent(Online_Play2.this, Online_Play1.class));
                finish();
            }
        });
        if (!((Activity) this).isFinishing()) {
            stateDialog = true;
            alert.show();
        }

    }

    public void rePlayGame() {
        rootDB.child("rooms")
                .child(mRoomID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomV2 room = task.getResult().getValue(RoomV2.class);
                    Map<String, Boolean> ready = room.getReady();
                    for (String key : ready.keySet()) {
                        ready.replace(key, false);
                    }
                    room.setReady(ready);
                    room.setWinUser("");
                    rootDB.child("rooms")
                            .child(mRoomID)
                            .setValue(room.toMap());

                    Intent intent = new Intent(Online_Play2.this, Online_PhongCho.class);
                    intent.putExtra("mRoomID", room.getPlayer1User());
                    intent.putExtra("mPlayerID", mAuth.getUid());
                    intent.putExtra("mPlayerUser", mPlayerUser);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Online_Play2.this, "Có lỗi xảy ra, vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showAlertConfirmLeaveRoom() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Thoát khỏi phòng");
        alert.setMessage("Bạn có chắc chắn muốn thoát phòng?");
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                startActivity(new Intent(Online_Play2.this, Online_Play1.class));
                finish();
            }
        });
        if (!((Activity) this).isFinishing()) {
            stateDialog = true;
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("IDEA", "onBackPressed Called");
        showAlertConfirmLeaveRoom();
    }
    private void deleteRoom() {
        rootDB.child("rooms").child(mRoomID).removeValue();
    }
    private void leaveRoom() {
        rootDB.child("rooms")
                .child(mRoomID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    RoomV2 room = task.getResult().getValue(RoomV2.class);
                    Map<String, Boolean> ready = new HashMap<>();
                    Map<String, String> playerIDs = new HashMap<>();
                    Map<String, Object> roomInfo = new HashMap<>();
                    String word = "";
                    String turn = mRoomID;
                    String winUser = "";
                    ready.put(mRoomID, false);

                    playerIDs.put("player1ID", mAuth.getUid());
                    playerIDs.put("player1User", mRoomID);

                    roomInfo.put("roomID", mRoomID);
                    roomInfo.put("playerIDs", playerIDs);
                    RoomV2 newRoom = new RoomV2(ready, roomInfo, word, turn, winUser);
                    Map<String, Object> map = newRoom.toMap();
                    rootDB.child("rooms").child(mRoomID).setValue(map);
                }
                else {
                    Toast.makeText(Online_Play2.this, "Đã xảy ra lỗi : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
