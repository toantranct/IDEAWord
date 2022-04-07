package com.ideastudio.ideaword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.Utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class Main5Play2Activity extends AppCompatActivity {
    private static final String filename = "com/ideastudio/ideaword/data/words.json";
    ImageButton imageButton;

    private TextView currentWord;
    private EditText playerWord;
    private TextView prefixPlayer;
    private Utils utils;

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
            Intent i = new Intent(Main5Play2Activity.this, Main2TrangchuActivity.class);
            startActivity(i);
        });

        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("toan", e.toString());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initView() throws IOException {
        playerWord = findViewById(R.id.playerWord);
        currentWord = findViewById(R.id.currentWord);
        prefixPlayer = findViewById(R.id.prefixPlayer);

        Gson gson = new Gson();

        String jsonFileString = utils.getJsonFromAssets(getApplicationContext(), "words.json");
        Type listUserType = new TypeToken<List<Dict>>() { }.getType();
        dicts = gson.fromJson(jsonFileString, listUserType);
        if (isStart) {
            // random word

            Random generator = new Random();
            int randomIndex = generator.nextInt(dicts.size() - 1);

            String randomWord = dicts.get(randomIndex).getWord();
            currentWord.setText(randomWord);
            String[] count = randomWord.split("\\s+");
            prefixPlayer.setText(count[1]);

            isStart = false;
        }
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
        Thread.sleep(300);
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
        String title = "May cho mày đấy con ạ";
        String supportText = "Khó thế cũng nghĩ ra được";
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(supportText)
                .setNeutralButton("Dell chơi nữa", (dialogInterface, i) -> {
                    // Respond to neutral button press
                    Intent intent = new Intent(Main5Play2Activity.this, Main2TrangchuActivity.class);
                    startActivity(intent);
                });


        alert.setPositiveButton("Kệ tao, tiếp tục", (dialogInterface, i) -> {
            // Respond to positive button press
            try {
                computerGenerateWord(convertToUnsignedString(playerWord.getText().toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showAlertWin() {
        String title = "OKOK =)) Từ này khó quá!";
        String supportText = "Chú mày là nhất.Ngon thì làm ván nữa.";
        MaterialAlertDialogBuilder alert;
        alert = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(supportText)
                .setNeutralButton("Dell chơi nữa", (dialogInterface, i) -> {
                    // Respond to neutral button press
                    Intent intent = new Intent(Main5Play2Activity.this, Main2TrangchuActivity.class);
                    startActivity(intent);
                });


        alert.setPositiveButton("OK bay. Lại", (dialogInterface, i) -> {
            // Respond to positive button press
            isStart = true;
            playerWord.setText("");
            try {
                initView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void showAlertLose() {
        String title = "Chết mịa mày con ơi";
        String supportText = "Dễ thế mà không nghĩ ra được";
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(supportText)
                .setNeutralButton("Trang chủ", (dialogInterface, i) -> {
                    // Respond to neutral button press
                    Intent intent = new Intent(Main5Play2Activity.this, Main2TrangchuActivity.class);
                    startActivity(intent);
                });


        alert.setPositiveButton("Chơi lại", (dialogInterface, i) -> {
            // Respond to positive button press
            isStart = true;
            playerWord.setText("");
            try {
                initView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        alert.show();
    }
}
