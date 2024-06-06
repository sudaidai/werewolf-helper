package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

import java.util.HashMap;

public class Seer extends Role {

    /**
     * 每夜可以查驗一位存活玩家的所屬陣營，並在白天透過發言向好人報出資訊。
     */

    public Seer() {
        stage = Action.預言家;
        imageId = R.drawable.seer;
        openSound = R.raw.seer_open;
        skillSound = R.raw.seer_skill;
        closeSound = R.raw.seer_close;
    }

    /**
     * @param seat 查驗座位
     * @return true狼人 false金水
     */
    public boolean see(int seat) {
        DataModel dataModel = DataModel.getInstance();
        HashMap<Action, Integer> wolfRoleMap = dataModel.getWolfRoleMap();
        if (dataModel.getWolfGroup().contains(seat)) {
            return true;
        } else if (wolfRoleMap.containsValue(seat)) {
            Action action = null;
            for (Action a : wolfRoleMap.keySet()) {
                if (wolfRoleMap.get(a) == seat) {
                    action = a;
                }
            }

            return action != Action.隱狼;
        }
        return false;
    }
}
