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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
                    Arrays.asList("ai oán", "ai ngờ", "am hiểu", "an lành", "an nhàn", "ắc quy", "ắng lặng", "âm lịch", "ấn độ", "ấn quỷ", "ấu trùng", "ân nhân", "búi tóc", "bước đều", "bước đi", "bánh mật", "bánh tráng", "con chó", "con cái", "cái mâm", "cậu cả", "con cưng", "cưng chiều", "dân trí", "dân thường", "diễn biến", "diễn giải", "diễn xuất", "đi đứng", "đứng yên", "đầm sen", "đĩ ngựa", "em yêu", "em gái", "em họ", "eo thon", "gan dạ", "gan lì", "ganh ghét", "ganh đua", "hoàng tử", "hoàng hậu", "hết đời", "huấn luyện", "hàm răng", "hàng hoá", "im lặng", "in ấn", "inh tai", "kĩ lưỡng", "kĩ càng", "khát tình", "khoai tây", "khoai môn", "khoan hồng", "khoái lạc", "khoáng vật", "lâm bệnh", "lâm sàng", "lâm sinh", "lâm bồn", "lâm chung", "lâu đài", "lãnh thổ", "lãnh địa", "mẹ con", "mẹ cha", "mãnh thú", "mắt nai", "nai tơ", "nạn nhân", "nhảy cầu", "nhảy dây", "ong chúa", "ong thợ", "ong vàng", "ong mật", "oán giận", "oán hận", "phai mờ", "phanh thây", "phi công", "phao câu", "phi pháp", "quân địch", "quân luật", "quân đội", "quân thù", "rung động", "rễ cọc", "rối loạn", "rộn ràng", "sức mạnh", "sức khoẻ", "sư phụ", "sưu tập", "sáng bóng", "trình độ", "than phiền", "tang lễ", "thương tang", "thất tình", "thất đức", "ung thư", "uy nghiêm", "uy danh", "uy nghi", "uốn tóc", "ước mơ", "ưu điểm", "ước ao", "ước định", "vòng tay", "vạn vật", "vận công", "vải kiện", "vấn đáp", "vấp ngã", "xiềng xích", "xích đu", "xích đạo", "xóm làng", "xôi lạc")
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
        Call<String> call;
        if (type == 3)
            call= remoteDict.getWords3(firstWord);
        else
            call = remoteDict.getWords4(firstWord);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String words = response.body();
                    availableWord.clear();

                    String firstWordStock = prefixPlayer.getText().toString();

                    if (words != null || !words.isEmpty())  {
                        Log.d("toan", words);
                        words = words.substring(9, words.length() - 2);
                        Gson gson = new Gson();
                        Dict wordsDict = gson.fromJson(words, Dict.class);
                        for (String sugg : wordsDict.getSuggestions()) {
                            // dem so cum tu co 2 tu
                            String[] count = sugg.split("\\s+");
                            if (count.length != 2) continue;
                            // kiem tra co tu can tim ko
                            Log.d("word", sugg);
                            currentPlayerWord = firstWordStock + " " + playerWord.getText().toString();
                            if (sugg.toLowerCase(Locale.ROOT).equals(currentPlayerWord.toLowerCase(Locale.ROOT))) {
                                availableWord.add(sugg);
                                break;
                            }
                        }
                    }

                    if (availableWord.isEmpty()) {
                        if (type == 4)
                            showAlertLose();
                        else
                            checkPlayerWord(firstWord, 4);
                        //Toast.makeText(Main5Play2Activity.this, "Ban da thua!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Main5Play2Activity.this, "Tu cua ban hop le!", Toast.LENGTH_SHORT).show();
                        // tiep tuc
                        showAlertContinue();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("toan", t.toString());
            }
        });
    }

    private void computerGenerateWord(String firstWord) throws InterruptedException {
        Thread.sleep(300);
        Call<String> call = remoteDict.getWords3(firstWord);
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.R)
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

        if (playerWord.getText().toString().isEmpty()) {
            Toast.makeText(Main5Play2Activity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
        }
        else {
            String[] pcWord = currentWord.getText().toString().split(" ");
            checkPlayerWord(convertToUnsignedString(pcWord[1]), 3);
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
                    Intent intent =new Intent(Main5Play2Activity.this,Main2TrangchuActivity.class);
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
                    Intent intent =new Intent(Main5Play2Activity.this,Main2TrangchuActivity.class);
                    startActivity(intent);
                });


        alert.setPositiveButton("OK bay. Lại", (dialogInterface, i) -> {
            // Respond to positive button press
            isStart = true;
            playerWord.setText("");
            initView();
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
                    Intent intent =new Intent(Main5Play2Activity.this,Main2TrangchuActivity.class);
                    startActivity(intent);
                });


        alert.setPositiveButton("Chơi lại", (dialogInterface, i) -> {
            // Respond to positive button press
            isStart = true;
            playerWord.setText("");
            initView();
        });

        alert.show();
    }
}
