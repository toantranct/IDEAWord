package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class Main8OptionsActivity extends AppCompatActivity {
    Button button1;
    MediaPlayer mediaPlayer;
    Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8_options);
        button1=(Button)findViewById(R.id.button8_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main8OptionsActivity.this,Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
        mediaPlayer = MediaPlayer.create(Main8OptionsActivity.this,R.raw.song);
        switch1=(Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(Main8OptionsActivity.this,"Bat",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                }else{
                    Toast.makeText(Main8OptionsActivity.this,"Tat",Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                }

            }
        });
    }
}