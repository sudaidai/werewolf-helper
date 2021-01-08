package com.example.werewolfs.wereWolfDev.model.job;

import android.util.Log;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.model.DataModel;
import com.example.werewolfs.wereWolfDev.model.Role;

import java.util.HashMap;

public class Shaman extends Role {
    private static final String TAG = "Shaman";

    public Shaman(){
        stage = Action.通靈師;
        openSound = R.raw.shaman_open;
        closeSound = R.raw.shaman_close;
        skillSound = R.raw.shaman_skill;
    }

    public Action seeThrough(int seat){
        Log.d(TAG, "seeThrough: " + seat);
        DataModel dataModel = Static.dataModel;
        HashMap<Action, Integer> tempRoleMap = dataModel.getGodRoleMap();
        for(Action stage : tempRoleMap.keySet()){
            if(tempRoleMap.get(stage) == seat){
                return stage;
            }
        }
        tempRoleMap = dataModel.getWolfRoleMap();
        for(Action stage : tempRoleMap.keySet()){
            if(tempRoleMap.get(stage) == seat){
                return stage;
            }
        }

        for(int s : dataModel.getWolfGroup()){
            if(seat == s){
                return Action.狼人;
            }
        }

        return Action.村民;
    }
}