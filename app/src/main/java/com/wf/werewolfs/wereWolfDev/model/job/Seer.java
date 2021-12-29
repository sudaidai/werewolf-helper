package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.constant.Static;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

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
