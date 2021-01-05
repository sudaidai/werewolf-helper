package com.example.werewolfs.wereWolfDev.activity;

import com.example.werewolfs.wereWolfDev.model.job.Seer;

import java.util.List;

public interface GameActivityNotify {

    void notifyRepeatSelect();
    void notifySeerAsk(boolean isWolf, int seat, Seer seer);
    void notifyDaybreak(String message);
    void notifyFirstDaybreak(String message, List<Integer> dieList);
    void notifyVoteCheck(int seat);
    void notifyGameEnd(String endTitle, String endMessage);

    /**
     * 告訴隱狼誰是他的隊友
     * @param wolfGroup
     */
    void notifyWolfFriend(List<Integer> wolfGroup);
}
