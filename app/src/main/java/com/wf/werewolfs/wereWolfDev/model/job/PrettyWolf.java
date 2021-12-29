package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class PrettyWolf extends Role {

    /**
     * 狼美人每晚參與殺人後，可單獨魅惑一名好人陣營的玩家。狼美人死亡時，
     * 當晚被魅惑的玩家隨之殉情。狼美人不能自刀或自爆。
     */

    private int lover;

    public PrettyWolf(){
        stage = Action.狼美人;
        openSound = R.raw.prettywolf_open;
        skillSound = R.raw.prettywolf_skill;
        closeSound = R.raw.prettywolf_close;
    }

    public void charm(int seat){
        lover = seat;
    }

    public int getLover() {
        return lover;
    }

    public void setLover(int lover) {
        this.lover = lover;
    }
}
