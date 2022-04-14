package com.ideastudio.ideaword.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.offline.Main3MyrankActivity;
import com.ideastudio.ideaword.offline.Main4Play1Activity;
import com.ideastudio.ideaword.offline.Main8OptionsActivity;
import com.ideastudio.ideaword.offline.Main9How2playActivity;

public class TrangchuonlineActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignOut, btnBot, btnFriend, btnRank, btnOption, btnHelp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchuonline);

        mAuth = FirebaseAuth.getInstance();

        InitView();
    }

    private void InitView() {
        btnSignOut = findViewById(R.id.button5);
        btnBot = findViewById(R.id.button1);
        btnFriend = findViewById(R.id.button);
        btnRank = findViewById(R.id.button2);
        btnOption = findViewById(R.id.button3);
        btnHelp = findViewById(R.id.button4);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == btnBot.getId()) {
            // choi voi may
            startActivity(new Intent(this, Main4Play1Activity.class));
        }
        else if (view.getId() == btnFriend.getId()) {
            //choi voi ban
            startActivity(new Intent(this, Online_Play1.class));
        } else if (view.getId() == btnRank.getId()) {
            //rank
            startActivity(new Intent(this, Main3MyrankActivity.class));
        }
        else if (view.getId() == btnOption.getId()) {
            //cai dat
            startActivity(new Intent(this, Main8OptionsActivity.class));
        }
        else if (view.getId() == btnHelp.getId()) {
            // huong dan
            startActivity(new Intent(this, Main9How2playActivity.class));
        }
        else if (view.getId() == btnSignOut.getId()) {
            Log.d("toan", "1");
            mAuth.signOut();
            startActivity(new Intent(TrangchuonlineActivity.this, LoginActivity.class));
        }
    }
}