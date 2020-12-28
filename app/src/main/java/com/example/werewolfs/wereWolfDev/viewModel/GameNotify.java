package com.example.werewolfs.wereWolfDev.viewModel;

import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.EndType;

public interface GameNotify {
    void notifyStageChanged(Action stage);
    void notifyNightEnd();
    void notifyGameEnd(EndType endType);
}
