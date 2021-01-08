package com.example.werewolfs.wereWolfDev.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.werewolfs.R;
import com.example.werewolfs.databinding.ActivityForCustomGameBinding;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ActivityForCustomGame extends AppCompatActivity {

    /** auto-generating class*/
    public ActivityForCustomGameBinding binding;

    private int wolf_cnt_temp; //畫面預設值 可以做Observe 暫用4
    private int peoCntChose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initValue();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_for_custom_game);
        binding.btnReturn.setOnClickListener(view -> finish());
        binding.btnAddWolf.setOnClickListener(view -> {
            if(wolf_cnt_temp > 2) {
                wolf_cnt_temp += 1;
                binding.textWolfCounts.setText(wolf_cnt_temp + "");
            }
        });

        binding.btnReduceWolf.setOnClickListener(view -> {
            if(wolf_cnt_temp > 2) {
                wolf_cnt_temp -= 1;
                binding.textWolfCounts.setText(wolf_cnt_temp + "");
            }
        });

        binding.btnStart.setOnClickListener(view ->{
            Static.dataModel.setWolfCnt(wolf_cnt_temp);
            setRole();
            setRule();
            Intent it = new Intent(ActivityForCustomGame.this, GameActivity.class);
            startActivity(it);
        });

        binding.witchSwitch.setOnCheckedChangeListener((view, isChecked) ->{
            if(isChecked){
                binding.witchSwitch.setText("女巫【可以】自救");
            }else{
                binding.witchSwitch.setText("女巫【不可】自救");
            }
        });

        binding.killSwitch.setOnCheckedChangeListener((view, isChecked)->{
            if(isChecked){
                binding.killSwitch.setText("採取【屠城】規則");
            }else{
                binding.killSwitch.setText("採取【屠邊】規則");
            }
        });

        binding.prettyWolfSwitch.setOnCheckedChangeListener((view, isChecked)->{
            if(isChecked){
                binding.prettyWolfSwitch.setText("狼美人夜晚死亡【可以】發動技能");
            }else{
                binding.prettyWolfSwitch.setText("狼美人夜晚死亡【不可】發動技能");
            }
        });

        binding.idiotDoubleKillSwitch.setOnCheckedChangeListener((view, isChecked)->{
            if(isChecked){
                binding.idiotDoubleKillSwitch.setText("白癡【有】雙面刀");
            }else{
                binding.idiotDoubleKillSwitch.setText("白癡【沒有】雙面刀");
            }
        });

        binding.seer.setOnCheckedChangeListener(characterListener);
        binding.witch.setOnCheckedChangeListener(characterListener);
        binding.hunter.setOnCheckedChangeListener(characterListener);
        binding.knight.setOnCheckedChangeListener(characterListener);
        binding.idiot.setOnCheckedChangeListener(characterListener);
        binding.bombMan.setOnCheckedChangeListener(characterListener);
        binding.forbiddenElder.setOnCheckedChangeListener(characterListener);
        binding.guard.setOnCheckedChangeListener(characterListener);
        binding.prettyWolf.setOnCheckedChangeListener(characterListener);
        binding.bear.setOnCheckedChangeListener(characterListener);
        binding.magician.setOnCheckedChangeListener(characterListener);
        binding.tombKeeper.setOnCheckedChangeListener(characterListener);
        binding.gargoyle.setOnCheckedChangeListener(characterListener);
        binding.shaman.setOnCheckedChangeListener(characterListener);
        binding.hiddenWolf.setOnCheckedChangeListener(characterListener);
        binding.wildChild.setOnCheckedChangeListener(characterListener);

        setTextTotalCount(peoCntChose);
    }

    private void initValue(){
        wolf_cnt_temp = 4;
        peoCntChose = 0;
    }

    private void setRule() {
        Static.dataModel.setWitchSaveRule(binding.witchSwitch.isChecked());
        Static.dataModel.setGameEndRule(binding.killSwitch.isChecked());
        Static.dataModel.setPrettyWolfRule(binding.prettyWolfSwitch.isChecked());
        Static.dataModel.setHunterKillRule(binding.hunterDoubleKillSwitch.isChecked());
    }

    private void setRole(){
        LinkedHashMap<Action, Boolean> role_map = Static.dataModel.getRoleMap();
        role_map.put(Action.隱狼, binding.hiddenWolf.isChecked());
        role_map.put(Action.狼美人, binding.prettyWolf.isChecked());
        role_map.put(Action.狐仙, false);
        role_map.put(Action.惡靈騎士, false);
        role_map.put(Action.陰陽使者, false);
        role_map.put(Action.女巫, binding.witch.isChecked());
        role_map.put(Action.石像鬼, binding.gargoyle.isChecked());
        role_map.put(Action.魔術師, binding.magician.isChecked());
        role_map.put(Action.預言家, binding.seer.isChecked());
        role_map.put(Action.熊, binding.bear.isChecked());
        role_map.put(Action.獵人, binding.hunter.isChecked());
        role_map.put(Action.騎士, binding.knight.isChecked());
        role_map.put(Action.白癡, binding.idiot.isChecked());
        role_map.put(Action.炸彈人, binding.bombMan.isChecked());
        role_map.put(Action.禁言長老, binding.forbiddenElder.isChecked());
        role_map.put(Action.守墓人, binding.tombKeeper.isChecked());
        role_map.put(Action.守衛, binding.guard.isChecked());
        role_map.put(Action.野孩子, binding.wildChild.isChecked());
        role_map.put(Action.混血兒, false);
        role_map.put(Action.盜賊, false);
        role_map.put(Action.九尾妖狐, false);
        role_map.put(Action.通靈師, binding.shaman.isChecked());
        role_map.put(Action.石像鬼2, binding.gargoyle.isChecked());
    }

    private void setTextTotalCount(int peoCntChose){
        binding.totalCount.setText("已選擇 : " + peoCntChose + " / " + Static.dataModel.getPeoCnt());
    }

    private CompoundButton.OnCheckedChangeListener characterListener = (buttonView, isChecked) ->
    {
        if(isChecked){
            peoCntChose += 1;
        }else{
            peoCntChose -= 1;
        }
        setTextTotalCount(peoCntChose);
    };
}
