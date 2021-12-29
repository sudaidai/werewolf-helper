package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Knight extends Role {

    public Knight(){
        stage = Action.騎士;
        openSound = R.raw.knight4;
        closeSound = R.raw.knight2;
    }
}
