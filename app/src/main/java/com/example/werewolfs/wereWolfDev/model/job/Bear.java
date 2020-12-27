package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;

public class Bear extends Role{

    public Bear(){
        stage = Action.熊;
        openSound = R.raw.bear_open;
        closeSound = R.raw.bear_close;
    }
}
