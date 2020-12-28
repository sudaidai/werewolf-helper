package com.example.werewolfs.wereWolfDev.model.job;


import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Wolf extends Role {

    public Wolf(){
        stage = Action.狼人;
        openSound = R.raw.wolf_open;
        openSound = R.raw.wolf_close;
    }

    /** 當晚將被擊殺的對象*/
    private int knifeOn;

    public void kill(int seat){
        knifeOn = seat;
    }

    public int getKnifeOn() {
        return knifeOn;
    }
}
