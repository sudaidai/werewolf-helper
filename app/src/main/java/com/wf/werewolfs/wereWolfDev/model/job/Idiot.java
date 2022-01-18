package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Idiot extends Role {

    /**
     * 白痴被投票出局，可以翻開自己的身份牌，免疫此次放逐，之後可以正常發言
     * ，但不能投票，狼人仍需要擊殺他才能讓他死亡。
     */

    public Idiot() {
        stage = Action.白癡;
        openSound = R.raw.idiot_open;
        closeSound = R.raw.idiot_close;
    }
}
