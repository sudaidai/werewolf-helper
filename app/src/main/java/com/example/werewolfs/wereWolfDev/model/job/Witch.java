package com.example.werewolfs.wereWolfDev.model.job;

public class Witch {

    private boolean hasHerbal;
    private boolean hasPoison;

    private int isSaved;
    private int isPoisoned;

    public Witch(){
        hasHerbal = true;
        hasPoison = true;
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
}
