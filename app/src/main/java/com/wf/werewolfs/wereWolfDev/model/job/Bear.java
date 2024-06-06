package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;

import java.util.List;

public class Bear extends Role {

    /**
     * 每天白天上帝宣布死亡信息之後會宣布熊是否咆哮。熊如果咆哮了，證明熊牌左右相鄰兩個位置有狼人牌，
     * 如果沒有咆哮則證明熊牌左右兩邊都是好人牌。
     */

    public Bear() {
        stage = Action.熊;
        imageId = R.drawable.bear;
        openSound = R.raw.bear_open;
        closeSound = R.raw.bear_close;
        skillSound = R.raw.bear_roar;
    }

    /**
     * @return 當天早上是否會吼叫
     */
    public boolean roar() {
        DataModel dataModel = DataModel.getInstance();
        int seat = getSeat();
        int peo_cnt = dataModel.getPeoCnt();
        List<Integer> dieList = dataModel.getDieList();
        List<Integer> wolfGroup = dataModel.getWolfGroup();
        int right = seat, left = seat;
        do {
            left = (left - 1) % peo_cnt;
            if (left == 0) {
                left = peo_cnt;
            }
        } while (dieList.contains(left));

        if (wolfGroup.contains(left)) {
            return true;
        }

        do {
            right = (right + 1) % peo_cnt;
            if (right == 0) {
                right = 1;
            }
        } while (dieList.contains(right));

        return wolfGroup.contains(right);
    }
}
