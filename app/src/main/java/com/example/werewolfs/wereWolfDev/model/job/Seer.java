package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.model.DataModel;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Seer extends Role {

    public Seer(){
        stage = Action.預言家;
        openSound = R.raw.seer_open;
        skillSound = R.raw.seer_skill;
        closeSound = R.raw.seer_close;
    }

    public boolean isWolf(int seat){
        return DataModel.getInstance().getWolfGroup().contains(seat);
    }
}
