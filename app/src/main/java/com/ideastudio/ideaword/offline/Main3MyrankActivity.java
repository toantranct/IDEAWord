package com.ideastudio.ideaword.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ideastudio.ideaword.R;
import com.ideastudio.ideaword.online.TrangchuonlineActivity;

public class Main3MyrankActivity extends AppCompatActivity {
    Button button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_myrank);
        button1=(Button)findViewById(R.id.button3_1);
        button2=(Button)findViewById(R.id.button3_2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main3MyrankActivity.this, TrangchuonlineActivity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main3MyrankActivity.this, Main4Play1Activity.class);
                startActivity(i);
            }
        });
    }
}