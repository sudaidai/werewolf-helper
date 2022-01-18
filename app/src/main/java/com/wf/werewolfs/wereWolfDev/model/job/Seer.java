package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.constant.Static;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Seer extends Role {

    /**
     * 每夜可以查驗一位存活玩家的所屬陣營，並在白天透過發言向好人報出資訊。
     */

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
