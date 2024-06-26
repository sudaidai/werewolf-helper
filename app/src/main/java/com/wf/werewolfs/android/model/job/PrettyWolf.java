package com.wf.werewolfs.android.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.model.Role;

public class PrettyWolf extends Role {

    /**
     * 狼美人每晚參與殺人後，可單獨魅惑一名好人陣營的玩家。狼美人死亡時，
     * 當晚被魅惑的玩家隨之殉情。狼美人不能自刀或自爆。
     */

    // 1. 沒有正常結束 狼美人與狼人皆死 2. 結束畫面顯示的內容狼美人變為村民

    private int lover;

    public PrettyWolf() {
        lover = 0;
        stage = Action.狼美人;
        imageId = R.drawable.pretty_wolf;
        openSound = R.raw.prettywolf_open;
        skillSound = R.raw.prettywolf_skill;
        closeSound = R.raw.prettywolf_close;
    }

    public void charm(int seat) {
        lover = seat;
    }

    public int getLover() {
        return lover;
    }
}
