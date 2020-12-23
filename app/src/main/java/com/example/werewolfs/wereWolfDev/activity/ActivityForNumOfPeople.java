package com.example.werewolfs.activity;
/** 用途 : 一般遊戲選擇人數*/
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.werewolfs.R;

public class ActivityForNumOfPeople extends AppCompatActivity {

    private TextView tv_show ;
    private int countPe = 10 ;
    Button btn_next;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_for_num_of_people);
        Button btn_b = findViewById(R.id.btn_b);
        btn_b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        tv_show = findViewById(R.id.tv_show);

        Button btn_down = findViewById(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view){
                if(countPe > 5) {
                    countPe = countPe - 1;
                    tv_show.setText("" + (countPe));
                }
            }
        });
        Button btn_up = findViewById(R.id.btn_up);
        btn_up.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view){
                if(countPe < 18) {
                    countPe = countPe + 1;
                    tv_show.setText("" + (countPe));
                }
            }
        });
        btn_next = findViewById(R.id.btn_next);
    }

    public void onNextChoose(View v){
        Intent it = new Intent(this, ActivityForNormalStart.class);
        it.putExtra("人數", countPe);
        startActivity(it);
    }
}
