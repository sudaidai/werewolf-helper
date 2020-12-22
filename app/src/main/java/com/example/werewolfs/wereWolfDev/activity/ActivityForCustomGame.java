package com.example.werewolfs.wereWolfDev.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.werewolfs.R;
import com.example.werewolfs.databinding.ActivityForCustomGameBinding;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.DataModel;

import java.util.HashMap;

public class ActivityForCustomGame extends AppCompatActivity {

    /** DataModel*/
    DataModel dataModel = new DataModel();

    /** auto-generating class*/
    private ActivityForCustomGameBinding binding;

    private int wolf_cnt_temp = 4; //畫面預設值 可以做Observe 暫用4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** data-binding*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_for_custom_game);
        binding.btnReturn.setOnClickListener(view -> finish());
        binding.btnAddWolf.setOnClickListener(view -> {
            if(wolf_cnt_temp > 2) {
                wolf_cnt_temp += 1;
                binding.textWolfCounts.setText(wolf_cnt_temp);
            }
        });

        binding.btnReduceWolf.setOnClickListener(view -> {
            if(wolf_cnt_temp > 2) {
                wolf_cnt_temp -= 1;
                binding.textWolfCounts.setText(wolf_cnt_temp);
            }
        });

        binding.btnStart.setOnClickListener(view ->{
            dataModel.setWolf_cnt(wolf_cnt_temp);
            setRole();
            setRule();
            Intent it = new Intent(this, GameActivity.class);
        });
    }

    private void setRule() {

    }

    private void setRole(){
        HashMap<Action, Boolean> role_map = dataModel.getRole_Map();
        role_map.put(Action.隱狼, binding.hiddenWolf.isChecked());
        role_map.put(Action.狼美人, binding.prettyWolf.isChecked());
        role_map.put(Action.狐仙, binding.foxGod.isChecked());
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
        role_map.put(Action.野孩子, false);
        role_map.put(Action.混血兒, false);
        role_map.put(Action.盜賊, false);
        role_map.put(Action.九尾妖狐, false);
        role_map.put(Action.通靈師, binding.shaman.isChecked());
        role_map.put(Action.石像鬼2, binding.gargoyle.isChecked());
    }
}
