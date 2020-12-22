package com.example.werewolfs.wereWolfDev.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.werewolfs.R;
import com.example.werewolfs.databinding.ActivityGameBinding;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.handler.GameHandlers;
import com.example.werewolfs.wereWolfDev.viewModel.GameViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameActivity extends AppCompatActivity {

    /** main class*/
    private GameViewModel gameViewModel;
    private GameHandlers handlers;

    /** view*/
    private ToggleButton[] tgBtn = new ToggleButton[19];

    /** auto-generating class*/
    private ActivityGameBinding binding;

    /** variable*/
    private int peopleN, totalWolf;
    private boolean canSaveSelf, killRules, prettyWolfNightSkill, doHunterKilled, doIdiotKilled;

    /** collection*/
    private LinkedHashMap<Action, Boolean> hasJob = new LinkedHashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** main class init*/
        gameViewModel = new GameViewModel(getApplication());
        handlers = new GameHandlers(gameViewModel);

        /** data_binding*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setViewModel(gameViewModel);
        binding.setCBField(gameViewModel.ctrlBtnField);
        binding.setHandlers(handlers);

        Intent it = getIntent();
        peopleN = it.getIntExtra("total_peo", 0);
        totalWolf = it.getIntExtra("total_wolf", 0);
        canSaveSelf = it.getBooleanExtra("canSaveSelf", false);
        killRules = it.getBooleanExtra("killRules", false);
        prettyWolfNightSkill = it.getBooleanExtra("prettyWolfNightSkill", false);
        doHunterKilled = it.getBooleanExtra("hunterDoubleKill", false);
        doIdiotKilled = it.getBooleanExtra("idiotDoubleKill", false);

        LinearLayout top = binding.top;
        LinearLayout bottom = binding.bottom;

        /** create toggleBtn by count of people*/
        for(int i=1 ; i <= peopleN/2 ; i++){
            top.addView(createToggleBtn(i));
        }
        //希望座位是順時鐘，下面的座位反序放入
        for(int i=peopleN ; i > peopleN/2 ; i--){
            bottom.addView(createToggleBtn(i));
        }

        HashMap<Action, Boolean> map = (HashMap<Action, Boolean>) getIntent().getExtras().getSerializable("roleMap");

        for(Action act : Action.values()){
            if(map.containsKey(act)){
                hasJob.put(act, map.get(act));
            } else{
                continue;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameViewModel.createSound();
        stageInit();
    }

    private void stageInit() {
        gameViewModel.setJob(hasJob);
        gameViewModel.setTotalWolf(totalWolf);
        gameViewModel.setPeopleNum(peopleN);
        gameViewModel.setTgBtn(tgBtn);
    }

    private ToggleButton createToggleBtn(int pos){
        tgBtn[pos] = new ToggleButton(this);
        tgBtn[pos].setId(pos);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        tgBtn[pos].setLayoutParams(params);
        tgBtnInit(tgBtn[pos]);
        return tgBtn[pos];
    }

    public void tgBtnInit(ToggleButton tgBtn){
        tgBtn.setText(tgBtn.getId() + "");
        tgBtn.setTextOn(tgBtn.getId() + "");
        tgBtn.setTextOff(tgBtn.getId() + "");
        tgBtn.setBackgroundColor(Color.WHITE);
        tgBtn.setTextColor(Color.BLACK);
        tgBtn.setClickable(false);
        tgBtn.setTextSize(40.0f);
    }

}
