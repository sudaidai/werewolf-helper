package com.wf.werewolfs.android.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.model.Role;

public class Hunter extends Role {

    /**
     * 在被票死或狼人刀死時可以開槍帶走一名玩家，但被女巫毒死或殉情而死不可開槍
     * ，每晚法官會告訴你技能是否還在???。
     */

    private int bulletOn = 0;

    public Hunter() {
        stage = Action.獵人;
        imageId = R.drawable.hunter;
        openSound = R.raw.hunter_open;
        closeSound = R.raw.hunter_close;
    }

    public int getBulletOn() {
        return bulletOn;
    }

    public void shoot(int seat) {
        if (getSeat() != seat) {
            bulletOn = seat;
        }
    }
}
