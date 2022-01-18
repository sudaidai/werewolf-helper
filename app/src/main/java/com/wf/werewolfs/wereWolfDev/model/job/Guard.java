package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Guard extends Role {

    /**
     * 每晚可以守護一名包括自己在內的玩家，該玩家不能被狼人殺害，但是不能連續兩晚守護同一人。
     */

    private int isProtected;

    public Guard() {
        isProtected = 0;
        stage = Action.守衛;
        openSound = R.raw.guard_open;
        skillSound = R.raw.guard_skill;
        closeSound = R.raw.guard_close;
    }

    public void protect(int seat) {
        isProtected = seat;
    }

    public int getIsProtected() {
        return isProtected;
    }
}
