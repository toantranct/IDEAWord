package com.ideastudio.ideaword;

import androidx.annotation.NonNull;
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
import com.ideastudio.ideaword.model.Dict;
import com.ideastudio.ideaword.remote.RemoteDict;

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
    private static final String baseURL = "https://vdict.com";

    ImageButton imageButton;
    private RemoteDict remoteDict;

    private TextView currentWord;
    private EditText playerWord;
    private TextView prefixPlayer;

    private String currentPlayerWord = "";
    private boolean isStart = true;

    List<String> availableWord = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_play2);
        imageButton=(ImageButton) findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(view -> {
            Intent i =new Intent(Main5Play2Activity.this,Main2TrangchuActivity.class);
            startActivity(i);
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
        //important
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(ScalarsConverterFactory.create()) //important
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        remoteDict = retrofit.create(RemoteDict.class);

        if (isStart)  {
            // random word
            ArrayList<String> listWord = new ArrayList<>(
                    Arrays.asList("Cậu cả", "quốc gia", "bàn học", "xinh đẹp", "nỗi buồn",
                            "hài hước",
                            "trẻ trâu")
            );
            Random generator = new Random();
            int randomIndex = generator.nextInt(listWord.size() - 1);

            String randomWord = listWord.get(randomIndex);

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

    private void checkPlayerWord(String firstWord, int type) {
        // tao mang gom cac tu co the tao
        Log.d("toan", firstWord);
        Call<String> call;
        if (type == 3)
            call= remoteDict.getWords3(firstWord);
        else
            call = remoteDict.getWords4(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String words = response.body();
                    availableWord.clear();
                    String secondWord = prefixPlayer.getText().toString();
                    secondWord = secondWord.toLowerCase(Locale.ROOT);

                    if (words.isEmpty()) {
                        if (type == 3) {
                            checkPlayerWord(firstWord, 4);
                            return;
                        }
                    }
                    else
                    {
                        Log.d("toan", words);
                        words = words.substring(9, words.length() - 2);
                        Gson gson = new Gson();
                        Dict wordsDict = gson.fromJson(words, Dict.class);
                        for (String sugg : wordsDict.getSuggestions()) {
                            // dem so cum tu co 2 tu
                            String[] count = sugg.split("\\s+");
                            if (count.length != 2) continue;
                            // kiem tra co tu can tim ko
                            if (!sugg.toLowerCase(Locale.ROOT).equals(currentWord.getText().toString().toLowerCase(Locale.ROOT)))
                                availableWord.add(sugg);
                        }
                    }

                    if (availableWord.isEmpty()) {
                        Toast.makeText(Main5Play2Activity.this, "Ban da thua!", Toast.LENGTH_SHORT).show();
                    } else {
                        String turnPlayerWord = secondWord + " " + playerWord.getText().toString();
                        for (String word : availableWord)
                            if (word.equals(turnPlayerWord)) {
                                Toast.makeText(Main5Play2Activity.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
                                currentPlayerWord = turnPlayerWord;
                                computerGenerateWord(convertToUnsignedString(playerWord.getText().toString()));
                                break;
                            }
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("toan", t.toString());
            }
        });
    }

    private void computerGenerateWord(String firstWord) {
        Call<String> call = remoteDict.getWords3(firstWord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String words = response.body();
                assert words != null;
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

                    // kiem tra co tu can tim ko
                    if (count[0].toLowerCase(Locale.ROOT).equals(firstWordStock) && !sugg.toLowerCase(Locale.ROOT).equals(currentPlayerWord.toLowerCase(Locale.ROOT))) {
                        availableWord.add(sugg);
                    }

                }

                if (availableWord.isEmpty()) {
                    Toast.makeText(Main5Play2Activity.this, "Ban da thang!", Toast.LENGTH_SHORT).show();
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

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d("toan", t.toString());
            }
        });
    }

    public void BtnCheckClick(View view) {
        //String[] pcWord = currentWord.getText().toString().split(" ");
        //currentSuffWord = pcWord[1];
        //checkPlayerWord(convertToUnsignedString(currentSuffWord));

        if (prefixPlayer.getText().toString().isEmpty()) {
            Toast.makeText(this, "Khong duoc de trong", Toast.LENGTH_SHORT).show();
        }
        else {
            String checkWord = convertToUnsignedString(prefixPlayer.getText().toString() + " " + playerWord.getText().toString());
            checkPlayerWord(checkWord,3);
        }

    }
}
