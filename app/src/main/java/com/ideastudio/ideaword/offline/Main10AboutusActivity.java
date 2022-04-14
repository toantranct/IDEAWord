package com.ideastudio.ideaword.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ideastudio.ideaword.R;

public class Main10AboutusActivity extends AppCompatActivity {
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10_aboutus);
        button1=(Button)findViewById(R.id.button10_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main10AboutusActivity.this, Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
    }
}