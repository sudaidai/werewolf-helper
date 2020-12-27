package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;

public class Witch extends Role{

    private boolean hasHerbal;
    private boolean hasPoison;

    private int isSaved;
    private int isPoisoned;

    public Witch(){
        stage = Action.女巫;
        hasHerbal = true;
        hasPoison = true;
        openSound = R.raw.witch_open;
        skillSound = R.raw.witch_skill;
        closeSound = R.raw.witch_close;
    }

    public void useHerbal(int seat){
        isSaved = seat;
        hasHerbal = false;
    }

    public void usePoison(int seat){
        isPoisoned = seat;
        hasPoison = false;
    }

    public boolean hasHerbal() {
        return hasHerbal;
    }

    public boolean hasPoison() {
        return hasPoison;
    }

    public int getIsSave() {
        return isSaved;
    }

    public int getIsPoisoned() {
        return isPoisoned;
    }
}
