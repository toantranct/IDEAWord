package com.ideastudio.ideaword.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.ideastudio.ideaword.R;

public class Online_PhongChoGuest extends AppCompatActivity {

ImageButton imageButton1,imageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongcho_guest);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton1) ;
        imageButton2=(ImageButton)findViewById(R.id.imagebutton2) ;
        imageButton1.setOnClickListener(view -> {
            Intent i = new Intent(Online_PhongChoGuest.this, Online_Play1.class);
            startActivity(i);
        });
        imageButton2.setOnClickListener(view -> {
            Intent i = new Intent(Online_PhongChoGuest.this,TrangchuonlineActivity.class);
            startActivity(i);
        });
    }

}