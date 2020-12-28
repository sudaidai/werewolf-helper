package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Guard extends Role {

    private int isProtected = 0;

    public Guard(){
        stage = Action.守衛;
        openSound = R.raw.guard_open;
        skillSound = R.raw.guard_skill;
        closeSound = R.raw.guard_close;
    }

    public void protect(int seat){
        isProtected = seat;
    }

    public int getIsProtected(){
        return isProtected;
    }
}