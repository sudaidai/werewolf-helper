package com.example.werewolfs.wereWolfDev.viewModel;

import com.example.werewolfs.wereWolfDev.constant.Action;

public interface GameNotify {
    void notifyAnnouncementChange(String str);
    void notifyPlaySound(int _id);
    void notifyStageChanged(Action stage);
}
