package com.example.werewolfs.wereWolfDev.constant;

import com.example.werewolfs.wereWolfDev.model.Role;
import com.example.werewolfs.wereWolfDev.model.job.Witch;

public enum Action {
    //有順序的，加職業的時候要按晚上睜眼順序放
    準備開始(0), 狼人(1), 殺人(2), 隱狼(3), 狼美人(4), 狐仙(5), 惡靈騎士(6),
    陰陽使者(7), 女巫(8), 石像鬼(9), 魔術師(10), 預言家(11), 熊(12),
    獵人(13), 騎士(14), 白癡(15), 炸彈人(16), 禁言長老(17), 守墓人(18),
    守衛(19), 野孩子(20), 混血兒(21), 盜賊(22), 九尾妖狐(23), 通靈師(24),
    石像鬼2(25), 白天(26);

    public int order;
    public Role role;

    Action(int order) {
        this.order = order;
    }

    Action(int order, Role role) {
        this.order = order;
        this.role = role;
    }

    /** 第一輪過後要跳過的階段*/
    public boolean isPassive(){
        if(order==0 || order==1 || order==3 || order==6 || order==9 ||
                (order>=12 && order<=16) || order==21 || order==22){
            return true;
        }
        return false;
    }
}
