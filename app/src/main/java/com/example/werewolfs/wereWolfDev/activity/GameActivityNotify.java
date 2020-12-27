package com.example.werewolfs.wereWolfDev.activity;

import com.example.werewolfs.wereWolfDev.model.job.Seer;

public interface GameActivityNotify {

    void notifyRepeatSelect();
    void notifySeerAsk(boolean isWolf, int seat, Seer seer);
}
