package com.example.werewolfs.wereWolfDev.activity;
/**
 * 用途 : 遊戲開啟介面
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.werewolfs.R;

public class StartActivity extends AppCompatActivity {

    private Button btn_custom_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_custom_game = findViewById(R.id.btn_custom_game);
        btn_custom_game.setOnClickListener(view -> {
            Intent it = new Intent(StartActivity.this, ActivityForCustomGame.class);
            startActivity(it);
        });
    }
}


