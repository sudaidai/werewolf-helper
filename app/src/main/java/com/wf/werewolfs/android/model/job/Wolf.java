package com.wf.werewolfs.android.model.job;


import com.wf.werewolfs.R;
import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.model.Role;

public class Wolf extends Role {

    /**
     * 當晚將被擊殺的對象
     */
    private int knifeOn;

    public Wolf() {
        knifeOn = 0;
        stage = Action.狼人;
        imageId = R.drawable.werewolf;
        openSound = R.raw.wolf_open;
        openSound = R.raw.wolf_close;
    }

    public void kill(int seat) {
        knifeOn = seat;
    }

    public int getKnifeOn() {
        return knifeOn;
    }
}
