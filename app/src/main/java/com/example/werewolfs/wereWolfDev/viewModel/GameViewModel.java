package com.example.werewolfs.wereWolfDev.viewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.SoundMgr;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.model.GameDataModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GameViewModel extends AndroidViewModel implements StageControlCallback {

    @Override
    public void callback(String announcement) {
        this.announcement.set(announcement);
    }

    /** observable object*/
    public static class CtrlBtnField{
        public final ObservableBoolean clickable = new ObservableBoolean();
        public final ObservableBoolean background = new ObservableBoolean(); //be white if true, black if false
        public final ObservableInt textColor = new ObservableInt();
        public final ObservableField<String> text = new ObservableField<>();

        CtrlBtnField(boolean clickable, boolean background, int textColor, String text){
            this.clickable.set(clickable);
            this.background.set(background);
            this.textColor.set(textColor);
            this.text.set(text);
        }
    }

    /** variable*/
    private final Context mContext; //to avoid memory leaks, this must be an Application context
    private SoundMgr music;
    private ToggleButton[] tgBtn;

    /** main class*/
    private GameDataModel gameDataModel = new GameDataModel(this);

    /** collection*/
    private List<Integer> tempGroup = new ArrayList<>();

    /**observable variable*/
    public final ObservableField<String> announcement = new ObservableField<>();
    public final CtrlBtnField ctrlBtnField = new CtrlBtnField(true, false, Color.WHITE, "夜晚");

    public GameViewModel(@NonNull Application app){
        super(app);
        this.mContext = app.getApplicationContext();
        init();
    }

    public GameDataModel getDataModel(){
        return gameDataModel;
    }

    private void init() {
        announcement.set("準備開始");
    }

    public void createSound(){ music = new SoundMgr(mContext); }

    /** get Data*/
    public SoundMgr getMedia(){
        return music;
    }
    public Context getContext(){
        return mContext;
    }

    /** set Data*/
    public void setJob(LinkedHashMap<Action, Boolean> hasJob) { gameDataModel.setJob(hasJob); }
    public void setStage(){
        gameDataModel.setStage();
    }
    public void setPeopleNum(int num){
        gameDataModel.setNum_People(num);
    }
    public void setTotalWolf(int num){ gameDataModel.setTotalWolf(num); }

    public void setTgBtn(ToggleButton[] tgBtn){
        this.tgBtn = tgBtn;
    }

    public void onDayClick(){
        if(gameDataModel.isDay()){
            gameDataModel.setDay();
            ctrlBtnField.clickable.set(false);
            ctrlBtnField.background.set(true);
            ctrlBtnField.textColor.set(Color.BLACK);

            switch (gameDataModel.getStage()){
                case 準備開始:
                    getMedia().playSound(getContext(), R.raw.howling);//狼嚎先不要

                    if (gameDataModel.getTurn() != 1) {
                        getMedia().playSound(getContext(), R.raw.wolf_open);
                    }
                    /** set all button of seat to black*/
                    for (int i = 1 ; i <= gameDataModel.getNum_People() ; i++) {
                        tgBtn[i].setBackgroundColor(Color.BLACK);
                        tgBtn[i].setTextColor(Color.WHITE);
                        tgBtn[i].setClickable(true);
                    }
                    setStage();
                    break;
            }
        }else{

        }
    }
}
