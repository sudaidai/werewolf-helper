package com.wf.werewolfs.android.constant;

import com.wf.werewolfs.android.model.Role;

public enum Action {
    //有順序的，加職業的時候要按晚上睜眼順序放
    準備開始(0), 狼人(1), 殺人(2), 狼美人(3), 隱狼(4), 狐仙(5), 惡靈騎士(6),
    陰陽使者(7), 女巫(8), 石像鬼(9), 魔術師(10), 預言家(11), 熊(12),
    守衛(13), 獵人(14), 騎士(15), 白癡(16), 炸彈人(17), 禁言長老(18), 守墓人(19),
    野孩子(20), 混血兒(21), 盜賊(22), 九尾妖狐(23), 通靈師(24),
    石像鬼2(25), 白天(26), 村民(27);

    public int order;
    public Role role;

    Action(int order) {
        this.order = order;
    }

    Action(int order, Role role) {
        this.order = order;
        this.role = role;
    }

    /**
     * 第一輪過後要跳過的階段
     */
    public boolean isPassive() {
        if (order == 0 || order == 1 || order == 4 || order == 6 || order == 9 ||
                 order == 12 || (order >= 15 && order <= 17) || order == 21 || order == 22) {
            return true;
        }
        return false;
    }
}
