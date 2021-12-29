package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class HiddenWolf extends Role {
    public HiddenWolf(){
        stage = Action.隱狼;
        openSound = R.raw.hidden_open;
        closeSound = R.raw.hidden_close;
    }
}
