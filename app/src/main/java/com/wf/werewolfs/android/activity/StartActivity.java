package com.wf.werewolfs.android.activity;
/**
 * 用途 : 遊戲開啟介面
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wf.werewolfs.R;
import com.wf.werewolfs.databinding.ActivityStartBinding;
import com.wf.werewolfs.android.viewModel.StartViewModel;

public class StartActivity extends AppCompatActivity {

    public ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartViewModel viewModel = new StartViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        binding.setViewModel(viewModel);

        binding.btnCustomGame.setOnClickListener(view -> {
            Intent it = new Intent(this, ActivityForNumOfPeople.class);
            startActivity(it);
        });
    }
}


