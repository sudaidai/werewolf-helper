package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class ForbiddenElder extends Role {

    private int muted;

    public ForbiddenElder(){
        stage = Action.禁言長老;
        openSound = R.raw.elder_open;
        closeSound = R.raw.elder_close;
        skillSound = R.raw.elder_skill;
    }

    public void mute(int seat){
        muted = seat;
    }

    public int getMuted() {
        return muted;
    }

    public void setMuted(int muted) {
        this.muted = muted;
    }
}
