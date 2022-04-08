package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2TrangchuActivity extends AppCompatActivity {
    Button button1,button2,button3,button4,button5;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_trangchu);
        button2=(Button)findViewById(R.id.button2);
        button1=(Button)findViewById(R.id.button1);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);
        tv1=(TextView)findViewById(R.id.textView);
        Intent i=getIntent();
        Bundle bundle =i.getExtras();
        String a = bundle.getString("ten");
        tv1.setText("Xin chào "+a);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main2TrangchuActivity.this,Main4Play1Activity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main2TrangchuActivity.this,Main3MyrankActivity.class);
                startActivity(i);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main2TrangchuActivity.this,Main8OptionsActivity.class);
                startActivity(i);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main2TrangchuActivity.this,Main9How2playActivity.class);
                startActivity(i);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main2TrangchuActivity.this,Main10AboutusActivity.class);
                startActivity(i);
            }
        });
    }
}