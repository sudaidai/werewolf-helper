package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Hunter extends Role {

    /**
     * 在被票死或狼人刀死時可以開槍帶走一名玩家，但被女巫毒死或殉情而死不可開槍
     * ，每晚法官會告訴你技能是否還在???。
     */

    public Hunter() {
        stage = Action.獵人;
        openSound = R.raw.hunter4;
        closeSound = R.raw.hunter2;
    }

    public boolean activeSkill() {
        return true;
    }
}
