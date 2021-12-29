package com.wf.werewolfs.wereWolfDev.model.job;

import android.util.Log;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.constant.Static;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

import java.util.HashMap;

public class Gargoyle extends Role {

    /**
     * 石像鬼不與狼隊相認，每晚可查驗一名玩家的實際身份。遊戲開始時不帶狼刀，當其餘狼同伴均被淘汰時，
     * 石像鬼帶刀，由上帝以手勢告知。石像鬼帶刀時，可先查驗玩家身份再行使狼刀。石像鬼不能自爆但可以自刀。
     */

    private static final String TAG = "Gargoyle";

    public Gargoyle(){
        stage = Action.石像鬼;
        openSound = R.raw.ghost_open;
        closeSound = R.raw.ghost_close;
        skillSound = R.raw.ghost_skill;
    }

    public Action seeThrough(int seat){
        Log.d(TAG, "seeThrough: " + seat);
        DataModel dataModel = DataModel.getInstance();
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
