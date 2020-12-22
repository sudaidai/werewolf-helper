package com.example.werewolfs.wereWolfDev.model;

import com.example.werewolfs.wereWolfDev.constant.Action;

import java.util.HashMap;

public class DataModel {

    /** 狼人數量*/
    private int wolf_cnt;
    /** 腳色有無*/
    private HashMap<Action, Boolean> role_Map = new HashMap<>();
    /** 遊戲規則*/
    private HashMap<String, Boolean> game_rule = new HashMap<>();

    public int getWolf_cnt() {
        return wolf_cnt;
    }

    public void setWolf_cnt(int wolf_cnt) {
        this.wolf_cnt = wolf_cnt;
    }

    public HashMap<Action, Boolean> getRole_Map() {
        return role_Map;
    }

    public HashMap<String, Boolean> getGame_rule() {
        return game_rule;
    }
}
