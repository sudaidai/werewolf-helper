package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Idiot extends Role {

    public Idiot(){
        stage = Action.白癡;
        openSound = R.raw.idiot_open;
        closeSound = R.raw.idiot_close;
    }
}
