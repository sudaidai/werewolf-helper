package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.model.DataModel;
import com.example.werewolfs.wereWolfDev.model.Role;

public class TombKeeper extends Role {

    private int inGrave;
    private boolean identity; //true -> 好人 false -> 壞人

    public TombKeeper(){
        stage = Action.守墓人;
        openSound = R.raw.tomb_open;
        closeSound = R.raw.tomb_close;
    }


    public int inGrave() {
        return inGrave;
    }

    public void bury(int inGrave) {
        DataModel dataModel = DataModel.getInstance();
        this.inGrave = inGrave;
        identity = !(dataModel.getWolfGroup().contains(inGrave) ||
        dataModel.getWolfRoleMap().values().contains(inGrave));
    }

    public boolean identify() {
        return identity;
    }
}
