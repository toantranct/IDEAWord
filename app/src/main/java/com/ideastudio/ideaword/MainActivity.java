package com.ideastudio.ideaword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText edt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.btntieptuc);
        edt1=(EditText) findViewById(R.id.Edt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String a = String.format(edt1.getText().toString());
                Intent i =new Intent(MainActivity.this,Main2TrangchuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("ten",a);
                i.putExtras(bundle);
                startActivity(i);

                */
                nextActivity();
            }

            private void nextActivity() {
                String strname = edt1.getText().toString().trim();
                AppUtil.mname = strname;

                Intent i =new Intent(MainActivity.this,Main2TrangchuActivity.class);
                startActivity(i);
            }
        });
    }
}