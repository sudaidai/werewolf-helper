package com.wf.werewolfs.wereWolfDev.model.job;

import com.wf.werewolfs.R;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.Role;

public class Witch extends Role {

    /**
     * 女巫擁有一瓶解藥和一瓶毒藥。女巫使用解藥前，可以在晚上得知當晚被狼隊殺害的對象，
     * 並決定是否使用解藥將其救活，但女巫不可以自救；
     * 女巫也可以利用白天所得資訊，將懷疑的對象毒殺，該對象若為獵人或狼王死後不能發動技能。
     * 解藥和毒藥不可以在同一夜使用
     */

    private boolean hasHerbal;
    private boolean hasPoison;

    private int isSaved;
    private int isPoisoned;

    public Witch() {
        stage = Action.女巫;
        hasHerbal = true;
        hasPoison = true;
        isSaved = 0;
        isPoisoned = 0;
        openSound = R.raw.witch_open;
        skillSound = R.raw.witch_skill;
        closeSound = R.raw.witch_close;
    }

    public void useHerbal(int seat) {
        if (hasHerbal) {
            isSaved = seat;
            hasHerbal = false;
        }
    }

    public void usePoison(int seat) {
        if (hasPoison) {
            isPoisoned = seat;
            hasPoison = false;
        }
    }

    public boolean hasMedicine() {
        return hasHerbal || hasPoison;
    }

    public boolean hasHerbal() {
        return hasHerbal;
    }

    public boolean hasPoison() {
        return hasPoison;
    }

    public void setIsSave(int isSaved) {
        this.isSaved = isSaved;
    }

    public int getIsSave() {
        return isSaved;
    }

    public void setIsPoisoned(int isPoisoned) {
        this.isPoisoned = isPoisoned;
    }

    public int getIsPoisoned() {
        return isPoisoned;
    }
}
