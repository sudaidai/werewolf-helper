package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

import java.util.ArrayList;
import java.util.List;

public class Magician extends Role {

    /**
     * 每天晚上你都可以選擇一個人和另外一個人互換，包括自己；
     * 但是一場遊戲裡面，每一個人只能被交換一次。
     */

    private final List<Integer> unchangeable;

    private int seat1;
    private int seat2;

    public Magician() {
        stage = Action.魔術師;
        seat1 = 0;
        seat2 = 0;
        openSound = R.raw.magic_open;
        skillSound = R.raw.magic_skill;
        closeSound = R.raw.magic_close;
        unchangeable = new ArrayList<>();
    }

    public void magic(int seat1, int seat2) {
        this.seat1 = seat1;
        this.seat2 = seat2;
        unchangeable.add(seat1);
        unchangeable.add(seat2);
    }

    public int getSeat1() {
        return seat1;
    }

    public void setSeat1(int seat1) {
        this.seat1 = seat1;
    }

    public int getSeat2() {
        return seat2;
    }

    public void setSeat2(int seat2) {
        this.seat2 = seat2;
    }
}
