package com.example.werewolfs.wereWolfDev.model.job;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.Role;

public class Witch extends Role {

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
        if(hasHerbal){
            isSaved = seat;
            hasHerbal = false;
        }
    }

    public void usePoison(int seat){
        if(hasPoison){
            isPoisoned = seat;
            hasPoison = false;
        }
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
