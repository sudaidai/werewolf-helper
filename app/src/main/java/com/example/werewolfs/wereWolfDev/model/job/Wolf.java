package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.wereWolfDev.constant.Static;

public class Wolf {

    /** 當晚將被擊殺的對象*/
    private int knifeOn;

    public void kill(int seat){
        knifeOn = seat;
    }

    public int getKnifeOn() {
        return knifeOn;
    }
}
