package com.wf.werewolfs.android.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.model.Role;

public class ForbiddenElder extends Role {

    /**
     * 禁言長老每晚可以選擇一名玩家進行禁言，該玩家在下一白天階段不能說話，
     * 只能以肢體語言表達想法。
     */

    private int muted;

    public ForbiddenElder() {
        muted = 0;
        stage = Action.禁言長老;
        imageId = R.drawable.forbidden_elder;
        openSound = R.raw.elder_open;
        closeSound = R.raw.elder_close;
        skillSound = R.raw.elder_skill;
    }

    public void mute(int seat) {
        muted = seat;
    }

    public int getMuted() {
        return muted;
    }

    public void setMuted(int muted) {
        this.muted = muted;
    }
}
