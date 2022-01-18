package com.wf.werewolfs.wereWolfDev.model.job;


import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Wolf extends Role {

    /**
     * 狼群
     */

    /** 當晚將被擊殺的對象*/
    private int knifeOn;

    public Wolf(){
        knifeOn = 0;
        stage = Action.狼人;
        openSound = R.raw.wolf_open;
        openSound = R.raw.wolf_close;
    }

    public void kill(int seat){
        knifeOn = seat;
    }

    public int getKnifeOn() {
        return knifeOn;
    }
}
