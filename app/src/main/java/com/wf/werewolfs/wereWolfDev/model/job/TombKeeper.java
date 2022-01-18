package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.constant.Static;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class TombKeeper extends Role {

    /**
     * 第二晚起能得知上一白天被放逐玩家所屬陣營為好人或為狼人。
     */

    private int inGrave;
    private boolean identity; //true -> 好人 false -> 壞人

    public TombKeeper(){
        inGrave = 0;
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
