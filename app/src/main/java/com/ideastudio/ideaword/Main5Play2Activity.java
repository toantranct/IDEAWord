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

import com.google.gson.GsonBuilder;

import java.text.Normalizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.remote.RemoteDict;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Main5Play2Activity extends AppCompatActivity {
    ImageButton imageButton;
    private RemoteDict remoteDict;
    private Retrofit retrofit;

    private TextView currentWord;
    private EditText playerWord;
    private TextView prefixPlayer;

    private String currentSuffWord;
    private boolean isStart = false;

    List<String> availableWord = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_play2);
        imageButton=(ImageButton) findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main5Play2Activity.this,Main9How2playActivity.class);
                startActivity(i);
            }
        });

        initView();

    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initView() {
        playerWord = findViewById(R.id.playerWord);
        currentWord = findViewById(R.id.currentWord);
        prefixPlayer = findViewById(R.id.prefixPlayer);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://vdict.com/")
                .addConverterFactory(ScalarsConverterFactory.create()) //important
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        remoteDict = retrofit.create(RemoteDict.class);

        if (isStart)  {
            // random word

            String randomWord = "động đất";
            currentWord.setText(randomWord);
            randomWord = convertToUnsignedString(randomWord);

            isStart = false;
            return;
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
        Log.d("toan", firstWord);
        Call<String> call = remoteDict.getWords3(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String words = response.body();
                    words = words.substring(9, words.length() - 2);
                    Gson gson = new Gson();
                    Dict wordsDict = gson.fromJson(words, Dict.class);
                    String secondWord = prefixPlayer.getText().toString();
                    secondWord = secondWord.toLowerCase(Locale.ROOT);
                    availableWord.clear();
                    for (String sugg : wordsDict.getSuggestions()) {
                        // dem so cum tu co 2 tu
                        String[] count = sugg.split("\\s+");
                        if (count.length != 2) continue;
                        Log.d("toan", sugg);
                        // kiem tra co tu can tim ko
                        if (!sugg.toLowerCase(Locale.ROOT).equals(currentWord.getText().toString().toLowerCase(Locale.ROOT)))
                            availableWord.add(sugg);

                    }
                    if (availableWord.isEmpty()) {
                        Toast.makeText(Main5Play2Activity.this, "Ban da thua!", Toast.LENGTH_SHORT).show();
                    } else {
                        String turnPlayerWord = secondWord + " " + playerWord.getText().toString();
                        for (String word : availableWord)
                            if (word.equals(turnPlayerWord)) {
                                Toast.makeText(Main5Play2Activity.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
                                currentWord.setText(turnPlayerWord);
                                computerGenerateWord(convertToUnsignedString(playerWord.getText().toString()));
                                break;
                            }
                    }



                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void computerGenerateWord(String firstWord) {
        Call<String> call = remoteDict.getWords3(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String words = response.body();
                words = words.substring(9, words.length() - 2);
                Gson gson = new Gson();
                Dict wordsDict = gson.fromJson(words, Dict.class);
                String firstWordStock = playerWord.getText().toString();
                firstWordStock = firstWordStock.toLowerCase(Locale.ROOT);
                availableWord.clear();
                for (String sugg : wordsDict.getSuggestions()) {
                    // dem so cum tu co 2 tu
                    String[] count = sugg.split("\\s+");
                    if (count.length != 2) continue;
                    Log.d("toan", sugg);
                    // kiem tra co tu can tim ko
                    if (count[0].toLowerCase(Locale.ROOT).equals(firstWordStock) && !sugg.toLowerCase(Locale.ROOT).equals(currentWord.getText().toString().toLowerCase(Locale.ROOT))) {
                        availableWord.add(sugg);
                    }

                }

                if (availableWord.isEmpty()) {
                    Toast.makeText(Main5Play2Activity.this, "Ban da thang!", Toast.LENGTH_SHORT).show();
                } else {
                    // tao ngau nhien trong mang sugg
                    Random generator = new Random();
                    int randomIndex = generator.nextInt(availableWord.size());
                    String turnComputerWord = availableWord.get(randomIndex);
                    currentWord.setText(turnComputerWord);
                    String[] count = turnComputerWord.split("\\s+");
                    prefixPlayer.setText(count[1]);
                    playerWord.setText("");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void BtnCheckClick(View view) {
        //String[] pcWord = currentWord.getText().toString().split(" ");
        //currentSuffWord = pcWord[1];
        //checkPlayerWord(convertToUnsignedString(currentSuffWord));

        String checkWord = convertToUnsignedString(prefixPlayer.getText().toString() + " " + playerWord.getText().toString());
        checkPlayerWord(checkWord);
    }
}
