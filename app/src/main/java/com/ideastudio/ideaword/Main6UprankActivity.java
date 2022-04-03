package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Main6UprankActivity extends AppCompatActivity {
    Button button1,button2;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_uprank);
        button1=(Button)findViewById(R.id.button6_1);
        button2=(Button)findViewById(R.id.button6_2);
        imageButton=(ImageButton) findViewById(R.id.imagebutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main6UprankActivity.this,Main4Play1Activity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main6UprankActivity.this,Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main6UprankActivity.this,Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
    }
}