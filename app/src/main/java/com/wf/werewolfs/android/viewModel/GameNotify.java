package com.wf.werewolfs.android.viewModel;

import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.constant.EndType;

public interface GameNotify {
    void notifyStageChanged(Action stage);

    void notifyDayEnd();

    void notifyNightEnd();

    void notifyGameEnd(EndType endType);

    /**
     * 如果狼隊友都死亡，隱狼會有刀人權力，同時被預言家查驗變為查殺
     */
    void notifyShowHiddenWolf();
}
