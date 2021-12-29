package com.wf.werewolfs.wereWolfDev.model;

import com.wf.werewolfs.wereWolfDev.constant.Action;

public class Role {
    public int openSound = -1;
    public int skillSound = -1;
    public int closeSound = -1;
    private int seat = 0;
    public boolean isAlive = true;
    public Action stage;

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public boolean unChecked(){
        return seat == 0;
    }

    public void killed(){
        isAlive = false;
    }

    public boolean activeSkill(){
        return false;
    }
}
