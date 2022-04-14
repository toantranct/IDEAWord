package com.ideastudio.ideaword.offline;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.model.Utils;
import com.ideastudio.ideaword.online.TrangchuonlineActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class Main5Play2Activity extends AppCompatActivity {
    ImageButton imageButton;

    private TextView currentWord;
    private EditText playerWord;
    private TextView prefixPlayer;
    private TextView countDownView;
    private TextView roundView;
    private TextView tvCurrentUser;
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_play2);
        imageButton = (ImageButton) findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(view -> {
            showAlertConfirmBack();
//            finish();
            //  Intent i = new Intent(Main5Play2Activity.this, Main2TrangchuActivity.class);
            //startActivity(i);
        });

        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("toan", e.toString());
        }
    }

    private void setRound(int round) {
        roundView.setText(String.valueOf(round));
    }

    private void initCountDown(int level) {
        if (countDownTimer == null)
            countDownTimer = new CountDownTimer(level * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    countDownView.setText(String.valueOf(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    //countDownView.setText("done!");
                    if (stateDialog == false)
                        showAlertLose();
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
            tvCurrentUser = findViewById(R.id.tvCurrentUser);
            Bundle extras = getIntent().getExtras();
            level = extras.getInt("level");
            isStart = false;
        }

        Gson gson = new Gson();

        String jsonFileString = utils.getJsonFromAssets(getApplicationContext(), "words.json");
        Type listUserType = new TypeToken<List<Dict>>() {
        }.getType();
        dicts = gson.fromJson(jsonFileString, listUserType);
        // random word

        Random generator = new Random();
        int randomIndex = generator.nextInt(dicts.size() - 1);

        String randomWord = dicts.get(randomIndex).getWord();
        currentWord.setText(randomWord);
        String[] count = randomWord.split("\\s+");
        prefixPlayer.setText(count[1]);
        currentRound = 1;
        initCountDown(level);
        setRound(currentRound++);
    }

    private String convertToUnsignedString(String word) {
        // chuyển về không dấu
        // if word have "đ" use "đ".Normalize(NormalizationForm.FormD) -- use loop process
        word = word.replaceAll("đ", "d");
        String dest = Normalizer.normalize(word, Normalizer.Form.NFD);
        dest = dest.replaceAll("[^\\p{ASCII}]", "");

        return dest;
    }

    private void checkPlayerWord(String firstWord)  {
        // tao mang gom cac tu co the tao
        availableWord.clear();

        String firstWordStock = prefixPlayer.getText().toString();

        if (dicts != null || !dicts.isEmpty()) {
            for (Dict dict : dicts) {
                // kiem tra co tu can tim ko
                currentPlayerWord = firstWordStock + " " + playerWord.getText().toString();
                if (dict.getWord().toLowerCase(Locale.ROOT).equals(currentPlayerWord.toLowerCase(Locale.ROOT))) {
                    availableWord.add(dict.getWord());
                    break;
                }
            }
        }

        if (availableWord.isEmpty()) {
            showAlertLose();
            //Toast.makeText(Main5Play2Activity.this, "Ban da thua!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Main5Play2Activity.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
            // tiep tuc
            showAlertContinue();
        }
    }


    private void computerGenerateWord(String firstWord) throws InterruptedException {
        tvCurrentUser.setText("Bot đang nghĩ");
        Thread.sleep(500);

        String firstWordStock = playerWord.getText().toString();
        firstWordStock = firstWordStock.toLowerCase(Locale.ROOT);

        availableWord.clear();
        for (Dict dict : dicts) {
            // dem so cum tu co 2 tu
            String[] count = dict.getWord().split("\\s+");
            if (count.length != 2) continue;
            // kiem tra co tu can tim ko
            if (count[0].toLowerCase(Locale.ROOT).equals(firstWordStock) && !dict.getWord().toLowerCase(Locale.ROOT).equals(currentPlayerWord.toLowerCase(Locale.ROOT))) {
                availableWord.add(dict.getWord());
            }

        }

        if (availableWord.isEmpty()) {
            showAlertWin();
            //Toast.makeText(Main5Play2Activity.this, "Ban da thang!", Toast.LENGTH_SHORT).show();
        } else {
            // tao ngau nhien trong mang sugg
            Random generator = new Random();
            int randomIndex = generator.nextInt(availableWord.size() - 1);
            String turnComputerWord = availableWord.get(randomIndex);
            currentWord.setText(turnComputerWord);
            String[] count = turnComputerWord.split("\\s+");
            prefixPlayer.setText(count[1]);
            currentPlayerWord = "";
            playerWord.setText("");
            initCountDown(level);
            setRound(currentRound++);
            tvCurrentUser.setText("Đến lượt bạn");
        }
    }

    public void BtnCheckClick(View view) {

        if (playerWord.getText().toString().isEmpty()) {
            Toast.makeText(Main5Play2Activity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
        } else {
            String[] pcWord = currentWord.getText().toString().split(" ");
            checkPlayerWord(convertToUnsignedString(pcWord[1]));
            //  String checkWord = convertToUnsignedString(prefixPlayer.getText().toString() + " " + playerWord.getText().toString());
            // checkPlayerWord(checkWord,3);
        }

    }

    public void showAlertContinue() {
        try {
            computerGenerateWord(convertToUnsignedString(playerWord.getText().toString()));
        } catch (InterruptedException e) {
            Log.d("IDEA_ERR", e.toString());
        }

//        Nếu show dialog
//        AlertDialog alert = new AlertDialog.Builder(this).create();
//        alert.setTitle("Chúc mừng, bạn đã chiến thắng !");
//        alert.setMessage("Bạn có muốn tiếp tục không?");
//        alert.setCanceledOnTouchOutside(false);
//        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Trang chủ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                alert.dismiss();
//            }
//        });
//        alert.setButton(AlertDialog.BUTTON_POSITIVE, "tiếp tục", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                isStart = true;
//                playerWord.setText("");
//                try {
//                    stateDialog = false;
//                    initView();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        stateDialog = true;
//        if (!((Activity) this).isFinishing()) {
//            alert.show();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showAlertWin() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Chúc mừng, bạn đã chiến thắng !");
        alert.setMessage("Bạn có muốn chơi tiếp không?");
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Trang chủ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                alert.dismiss();
            }
        });
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Chơi lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isStart = true;
                playerWord.setText("");
                try {
                    stateDialog = false;
                    initView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stateDialog = true;
        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showAlertLose() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Bạn đã thua !");
        alert.setMessage("Bạn có muốn phục thù không?");
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Trang chủ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // stateDialog = false;
               // alert.dismiss();
                 Intent ii = new Intent(Main5Play2Activity.this, TrangchuonlineActivity.class);
                startActivity(ii);
            }
        });
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Chơi lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isStart = true;
                playerWord.setText("");
                try {
                    stateDialog = false;
                    initView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stateDialog = true;
        if (!((Activity) this).isFinishing()) {
            alert.show();
        }

    }

    public void showAlertConfirmBack() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Xác nhận");
        alert.setMessage("Bạn có chắc chắn muốn thoát?");
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                alert.dismiss();
            }
        });
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stateDialog = false;
                backToHome();
            }
        });
        stateDialog = true;
        if (!((Activity) this).isFinishing()) {
            alert.show();
        }
    }

    public void backToHome() {
        Intent i = new Intent(Main5Play2Activity.this, TrangchuonlineActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Do extra stuff here
        finish();
    }
}

