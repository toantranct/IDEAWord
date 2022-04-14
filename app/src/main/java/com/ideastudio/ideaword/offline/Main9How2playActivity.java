package com.ideastudio.ideaword.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ideastudio.ideaword.R;

public class Main9How2playActivity extends AppCompatActivity {
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9_how2play);
        button1=(Button)findViewById(R.id.button9_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main9How2playActivity.this, Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
    }
}