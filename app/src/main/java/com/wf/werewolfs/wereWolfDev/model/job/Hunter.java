package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Hunter extends Role {

    public Hunter(){
        stage = Action.獵人;
        openSound = R.raw.hunter4;
        closeSound = R.raw.hunter2;
    }

    public boolean activeSkill(){
        return true;
    }
}
