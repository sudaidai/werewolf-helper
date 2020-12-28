package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class HiddenWolf extends Role {
    public HiddenWolf(){
        stage = Action.隱狼;
        openSound = R.raw.hidden_open;
        closeSound = R.raw.hidden_close;
    }
}
