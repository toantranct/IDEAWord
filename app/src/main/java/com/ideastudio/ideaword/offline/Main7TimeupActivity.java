package com.ideastudio.ideaword.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ideastudio.ideaword.R;

public class Main7TimeupActivity extends AppCompatActivity {
    Button button1,button2;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7_timeup);
        button1=(Button)findViewById(R.id.button7_1);
        button2=(Button)findViewById(R.id.button7_2);
        imageButton=(ImageButton) findViewById(R.id.imagebutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main7TimeupActivity.this, Main4Play1Activity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main7TimeupActivity.this, Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main7TimeupActivity.this,Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
    }
}