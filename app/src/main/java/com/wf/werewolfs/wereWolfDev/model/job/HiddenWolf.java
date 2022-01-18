package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class HiddenWolf extends Role {

    /**
     * 不知道自己狼隊友是誰，但狼人勝利則隱狼一起勝利，隱狼在熊旁邊，熊不咆嘯、預言家驗隱狼為金水。
     * (不會進入wolf group, 會被放入wolf role group, 查驗與熊都是判斷wolf group)
     */

    public HiddenWolf(){
        stage = Action.隱狼;
        openSound = R.raw.hidden_open;
        closeSound = R.raw.hidden_close;
    }
}
