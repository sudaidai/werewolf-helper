package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Knight extends Role {

    public Knight(){
        stage = Action.騎士;
        openSound = R.raw.knight4;
        closeSound = R.raw.knight2;
    }
}
