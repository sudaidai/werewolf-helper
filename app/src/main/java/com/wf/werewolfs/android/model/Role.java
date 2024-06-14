package com.wf.werewolfs.android.model;

import com.wf.werewolfs.android.constant.Action;

public class Role {
    public int openSound = -1;
    public int skillSound = -1;
    public int closeSound = -1;
    private int seat = -1;
    public boolean isAlive = true;
    public Action stage;
    public int imageId;

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public boolean unChecked() {
        return seat == -1;
    }

    public void killed() {
        isAlive = false;
    }
}
