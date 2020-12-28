package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Idiot extends Role {

    public Idiot(){
        stage = Action.白癡;
        openSound = R.raw.idiot_yc_open;
        closeSound = R.raw.idiot_yc_close;
    }
}
