package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Knight extends Role {

    /**
     * 可以在白天投票前的任何時候（上警階段除外）翻出底牌，並指定一名玩家
     * ，由主持人宣佈，該玩家是否是狼人。 若是狼人，則此玩家立即死亡，白天結束
     * ，馬上進入晚上。 如果不是，則騎士以死謝罪，當天的投票繼續。
     */

    public Knight(){
        stage = Action.騎士;
        openSound = R.raw.knight4;
        closeSound = R.raw.knight2;
    }
}
