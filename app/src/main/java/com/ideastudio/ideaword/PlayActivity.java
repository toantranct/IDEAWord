package com.ideastudio.ideaword;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.remote.RemoteDict;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PlayActivity extends AppCompatActivity {
    private RemoteDict remoteDict;
    private Retrofit retrofit;

    private TextView currentWord;
    private EditText  playerWord;
    private TextInputLayout textPlayer;

    private String computerSuffWord;
    private String prefixWord;
    private boolean isStart = false;
    List<String> availableWord = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        CharSequence s = "ccc";
        textPlayer.setPrefixText(s);

        initView();
    }

    private void initView() {
        playerWord = findViewById(R.id.playerWord);
        currentWord = findViewById(R.id.currentWord);

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

            //getWords(randomWord);
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
        Call<String> call = remoteDict.getWords(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String words = response.body();
                    words = words.substring(9, words.length() - 2);
                    Gson gson = new Gson();
                    Dict wordsDict = gson.fromJson(words, Dict.class);
                    String secondWord = computerSuffWord;
                    secondWord = secondWord.toLowerCase(Locale.ROOT);
                    availableWord.clear();
                    for (String sugg : wordsDict.getSuggestions()) {
                        // dem so cum tu co 2 tu
                        String[] count = sugg.split("\\s+");
                        if (count.length != 2) continue;
                        // kiem tra co tu can tim ko
                        if (count[0].toLowerCase(Locale.ROOT).equals(secondWord) && !sugg.equals(currentWord.getText().toString()))
                            availableWord.add(sugg);
                    }

                    String turnPlayerWord = secondWord + " " + playerWord.getText().toString();
                    for (String word : availableWord)
                        if (word.equals(turnPlayerWord)) {
                            Log.d("toan", word);
                            Toast.makeText(PlayActivity.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
                            currentWord.setText(turnPlayerWord);
                            computerGenerateWord(convertToUnsignedString(playerWord.getText().toString()));
                            break;
                        }


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void computerGenerateWord(String firstWord) {
        Call<String> call = remoteDict.getWords(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String words = response.body();
                words = words.substring(9, words.length() - 2);
                Gson gson = new Gson();
                Dict wordsDict = gson.fromJson(words, Dict.class);
                String secondWord = computerSuffWord;
                secondWord = secondWord.toLowerCase(Locale.ROOT);
                String firstWordStock = playerWord.getText().toString();
                firstWordStock = firstWordStock.toLowerCase(Locale.ROOT);
                availableWord.clear();
                for (String sugg : wordsDict.getSuggestions()) {
                    // dem so cum tu co 2 tu
                    String[] count = sugg.split("\\s+");
                    if (count.length != 2) continue;
                    // kiem tra co tu can tim ko
                    if (count[0].toLowerCase(Locale.ROOT).equals(firstWordStock) && !sugg.equals(currentWord.getText().toString()))
                        availableWord.add(sugg);

                    }

                if (availableWord.size() == 0) {
                    Toast.makeText(PlayActivity.this, "Ban da thang!", Toast.LENGTH_SHORT).show();
                } else {
                    // tao ngau nhien trong mang sugg
                    Random generator = new Random();
                    int randomIndex = generator.nextInt(availableWord.size());
                    String turnComputerWord = availableWord.get(randomIndex);
                    currentWord.setText(turnComputerWord);
                    String[] count = turnComputerWord.split("\\s+");
                    textPlayer.setPrefixText("lì");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getWords(String word) {
        Call<String> call = remoteDict.getWords(word);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String words = response.body();
                    words = words.substring(9, words.length() - 2);
                    Gson gson = new Gson();
                    Dict wordsDict = gson.fromJson(words,
                            Dict.class);
                    String secondWord = "động";
                    secondWord = secondWord.toLowerCase(Locale.ROOT);
                    for (String sugg : wordsDict.getSuggestions()) {
                        // dem so cum tu co 2 tu
                        String[] count = sugg.split("\\s+");
                        if (count.length != 2) continue;
                        // kiem tra co tu can tim ko
                        if (count[0].toLowerCase(Locale.ROOT).equals(secondWord))
                            Log.d("word", sugg);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("word", t.toString());
            }
        });

    }
    public void BtnCheckClick(View view) {
        String[] pcWord = currentWord.getText().toString().split(" ");
        computerSuffWord = pcWord[1];
        checkPlayerWord(convertToUnsignedString(computerSuffWord));
    }
}
