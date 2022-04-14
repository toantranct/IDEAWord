package com.ideastudio.ideaword.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.online.TrangchuonlineActivity;

public class Main4Play1Activity extends AppCompatActivity {
    Button button1,button2;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_play1);
        button1=(Button)findViewById(R.id.button4_1);
        button2=(Button)findViewById(R.id.button4_2);
        imageButton=(ImageButton) findViewById(R.id.imagebutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main4Play1Activity.this, Main5Play2Activity.class);
                i.putExtra("level", 21);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main4Play1Activity.this,Main5Play2Activity.class);
                i.putExtra("level", 11);
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //   finish();
                Intent i =new Intent(Main4Play1Activity.this, TrangchuonlineActivity.class);
               startActivity(i);
            }
        });
    }
}