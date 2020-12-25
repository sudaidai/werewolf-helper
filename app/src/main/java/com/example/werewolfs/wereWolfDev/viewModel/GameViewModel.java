package com.example.werewolfs.wereWolfDev.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.SoundMgr;
import com.example.werewolfs.wereWolfDev.activity.GameActivityNotify;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.model.DataModel;
import com.example.werewolfs.wereWolfDev.model.job.Witch;
import com.example.werewolfs.wereWolfDev.model.job.Wolf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameViewModel extends AndroidViewModel implements GameNotify{

    private static final String TAG = "GameViewModel";
    private GameActivityNotify gameActivityNotify;

    /** 儲存使用者行為 當前被選擇對象 上一次被選擇對象(用於單選)*/
    private int selected, lastSelect;
    /** 儲存使用行為 被選擇對象數量(用於多選)*/
    private List<Integer> seatsSelected;

    /**
     * 身分與職業
     */
    private Wolf wolves;
    private Witch witch;

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
    private ToggleButton[] tgBtnGroup;

    /**observable variable*/
    public final ObservableField<String> announcement = new ObservableField<>();
    public final ObservableField<String> checkText = new ObservableField<>();
    public final ObservableBoolean checkVisible = new ObservableBoolean();
    public final CtrlBtnField ctrlBtnField = new CtrlBtnField(true, false, Color.WHITE, "夜晚");

    public GameViewModel(@NonNull Application app){
        super(app);
        this.mContext = app.getApplicationContext();
        init();
    }

    public void setGameActivityNotify(GameActivityNotify gameActivityNotify) {
        this.gameActivityNotify = gameActivityNotify;
    }

    private void init() {
        announcement.set("準備開始");
        initSelect();
        seatsSelected = new ArrayList<>();
        wolves = new Wolf();
        witch = new Witch();
        Static.dataModel.setGameNotify(this);
    }

    public void initSelect() {
        selected = -1;
        lastSelect = -1;
    }

    public void initGameVariable() {
        Static.dataModel.initGameVariable();
    }

    public void createSound(){ music = new SoundMgr(mContext); }

    public void setTgBtn(ToggleButton[] tgBtnGroup){
        this.tgBtnGroup = tgBtnGroup;
        for(int i=1 ; i<=Static.dataModel.getPeoCnt() ; i++){
            tgBtnGroup[i].setOnCheckedChangeListener(onSeatClick);
        }
    }

    /**
     * 中間按鈕點擊事件
     */
    public void onCenterClick(){
        DataModel dataModel = Static.dataModel;
        if(dataModel.isDay()) {
            music.playSound(R.raw.howling);

            dataModel.dayEnd();
            ctrlBtnField.textColor.set(Color.BLACK);
            ctrlBtnField.background.set(true);
            ctrlBtnField.clickable.set(false);

            switch (dataModel.getStage()){
                case 準備開始:
                    setAllTgBtnState(true);
                    setAllTgBtnStyle();
                    dataModel.setNextStage();
            }
        } else {
            switch (dataModel.getStage()){
                case 守衛:
                    break;
                case 禁言長老:
                    break;
                default:
                    ctrlBtnField.text.set("夜晚");
                    ctrlBtnField.clickable.set(false);
                    //initSelect(); ?? TODO
                    dataModel.setNextStage();
            }
        }
    }

    /**
     * 座位點擊事件
     */
    private CompoundButton.OnCheckedChangeListener onSeatClick = ((buttonView, isChecked) -> {
        int seat = findIndexByBtnArray(buttonView);
        Action stage = Static.dataModel.getStage();
        Log.d(TAG, "findIndexByBtnGroup(Seat): " + seat);

        if(isChecked){
            checkedEvent(seat, stage);
        }else{
            if (Static.dataModel.isDay()){
                buttonView.setBackgroundColor(Color.WHITE);
            }else{
                buttonView.setBackgroundColor(Color.BLACK);
            }
            unCheckedEvent(seat, stage);
        }
    });

    private void checkedEvent(int seat, Action stage) {
        ToggleButton tgBtn = tgBtnGroup[seat];
        DataModel dataModel = Static.dataModel;
        HashMap<Action, Integer> seatRoleMap = dataModel.getSeatRoleMap();
        switch(stage){
            case 狼人:
                setColorAndTextOn(tgBtn, "狼人", Color.BLUE);
                seatsSelected.add(seat);
                if(seatsSelected.size() == dataModel.getWolfCnt()){
                    showCheckBtn("狼人為 : " + seatsSelected + " \n確認是否為您的夥伴");
                }break;
            case 殺人:
                setColorAndTextOn(tgBtn, "擊殺", Color.RED);
                choosePosition(seat);
                break;
            case 女巫:
                boolean witchChecked = seatRoleMap.containsKey(Action.女巫);
                choosePosition(seat);
                if(!witchChecked){
                    setColorAndTextOn(tgBtn, "女巫", Color.BLUE);
                } else {
                    if(dataModel.isWitchSaveRule() && dataModel.getTurn()==1){
                        //女巫可以自救的情況
                        if(wolves.getKnifeOn() == seat && witch.hasHerbal()){
                            setColorAndTextOn(tgBtn, "拯救?", Color.BLUE);
                        }else if(seatRoleMap.get(stage) == selected){
                            setColorAndTextOn(tgBtn, "不用藥?", Color.BLUE);
                        }else{
                            setColorAndTextOn(tgBtn, "毒殺?", Color.RED);
                        }
                    }else{
                        if(seatRoleMap.get(stage) == selected){
                            setColorAndTextOn(tgBtn, "不用藥?", Color.BLUE);
                        }else if(wolves.getKnifeOn() == selected && witch.hasHerbal()){
                            setColorAndTextOn(tgBtn, "拯救?", Color.BLUE);
                        }else{
                            setColorAndTextOn(tgBtn, "毒殺?", Color.RED);
                        }
                    }
                }break;
        }
    }

    private void unCheckedEvent(int seat, Action stage) {
        DataModel dataModel = Static.dataModel;
        HashMap<Action, Integer> seatRoleMap = dataModel.getSeatRoleMap();
        switch(stage){
            case 狼人:
                seatsSelected.remove(new Integer(seat));
                break;
            case 殺人:
                if(selected == seat){
                    wolves.kill(seat);
                    initSelect();
                    music.playSound(R.raw.wolf_close);
                    dataModel.setNextStage();
                }break;
            case 女巫:
                if(seat == selected) {
                    boolean witchChecked = seatRoleMap.containsKey(Action.女巫);
                    if (!witchChecked) {
                        repeatSelectionCheck(seat);
                        announcement.set("女巫請使用技能");
                        music.playSound(R.raw.witch_skill);
                        seatRoleMap.put(stage, seat);
                        Log.d(TAG, "女巫為 : " + seat + "號玩家");
                        //第一輪尚未確定誰是女巫，確認後跳出被刀的對象
                        tgBtnGroup[wolves.getKnifeOn()].setBackgroundColor(Color.RED);
                    } else {
                        //如果女巫沒有藥，在進入女巫stage不能用藥對象要被鎖定(notifyStageChanged)
                        if(witch.hasHerbal() && wolves.getKnifeOn() == seat){
                            witch.useHerbal(seat);
                        }else if(witch.hasPoison() && seatRoleMap.get(stage) != seat){
                            witch.usePoison(seat);
                        }
                        music.cancelSound();
                        dataModel.setNextStage();
                    }
                    initSelect();
                }break;
        }
    }

    /**
     * 中央提示按鈕點擊事件
     */

    public void onYesClick(){
        DataModel dataModel = Static.dataModel;
        List<Integer> wolves = dataModel.getWolfGroup();
        wolves.addAll(seatsSelected);

        for(int seat : wolves){
            dataModel.getSeatRoleMap().put(Action.狼人, seat);
            setPositionFalse(seat);
        }
        checkVisible.set(false);
        Log.d(TAG, "wolves-> " + wolves);
        Static.dataModel.setNextStage();
    }

    public void onNoClick(){
        List<Integer> temp = new ArrayList<>();
        temp.addAll(seatsSelected);
        for(int seat : temp){
            setPositionFalse(seat);
        }
        checkVisible.set(false);
    }



    /**
     * 處理玩家選擇行為顯示
     * @param selected
     */
    public void choosePosition(int selected) {
        if (this.selected == -1) {
            this.selected = selected;
        } else {
            this.selected = selected;
            setPositionFalse(lastSelect);
        }
        lastSelect = selected;
    }

    /**
     * 取消選取行為顯示內容
     * @param pos
     */
    public void setPositionFalse(int pos) {
        tgBtnGroup[pos].setChecked(false);
        if (Static.dataModel.isDay()) {
            tgBtnGroup[pos].setBackgroundColor(Color.WHITE);
        } else {
            tgBtnGroup[pos].setBackgroundColor(Color.BLACK);
        }
        tgBtnGroup[pos].setText(pos + "");
    }

    /**
     * 改變全部座位可否被點擊
     * @param bool
     */
    private void setAllTgBtnState(boolean bool){
        for(int i=1 ; i<=Static.dataModel.getPeoCnt() ; i++){
            tgBtnGroup[i].setClickable(bool);
        }
    }

    /**
     * 改變全部座位顏色
     */
    private void setAllTgBtnStyle(){
        int textColor, bgColor;
        if(Static.dataModel.isDay()){
            textColor = Color.BLACK;
            bgColor = Color.WHITE;
        }else{
            textColor = Color.WHITE;
            bgColor = Color.BLACK;
        }

        for(int i=1 ; i<=Static.dataModel.getPeoCnt() ; i++){
            tgBtnGroup[i].setTextColor(textColor);
            tgBtnGroup[i].setBackgroundColor(bgColor);
        }
    }

    /**
     * 設置單個按鈕點開的顏色與文字
     * @param tgBtn
     * @param text
     * @param color
     */
    public void setColorAndTextOn(ToggleButton tgBtn, String text, int color) {
        tgBtn.setTextOn(text);
        tgBtn.setBackgroundColor(color);
    }

    /**
     * 從按鈕群找到按鈕的index
     * @param btn
     * @return
     */
    private int findIndexByBtnArray(CompoundButton btn){
        for(int i=0 ; i<tgBtnGroup.length ; i++){
            if(tgBtnGroup[i] == btn) return i;
        }
        return -1;
    }

    /**
     * 跳出中央選擇提示框
     * @param checkText
     */
    private void showCheckBtn(String checkText) {
        checkVisible.set(true);
        this.checkText.set(checkText);
    }

    /**
     * 該玩家死亡，晚上顯示跳過階段
     */
    public void skipStage() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                announcement.set(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                ctrlBtnField.text.set("點我跳過");
                ctrlBtnField.clickable.set(true);
            }
        }.start();
    }

    /**
     * 進入下一階段，開眼與閉眼間格時間，請玩家睜眼
     */
    public void stageDelay(Action stage) {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                announcement.set(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                announcement.set(stage + "請睜眼");
//                switch (stage){
//                    case 女巫:
//                        if (witch.hasHerbal()) {
//                            tgBtnGroup[wolves.getKnifeOn()].setBackgroundColor(Color.RED);
//                        }break;
//                }
            }
        }.start();
    }

    /**
     * 檢查玩家確認後腳色是否重複
     * @param seat
     */
    public void repeatSelectionCheck(int seat) {
        if (Static.dataModel.getSeatRoleMap().values().contains(seat)) {
            gameActivityNotify.notifyRepeatSelect();
        } else {
            return;
        }
    }





    @Override
    public void notifyAnnouncementChange(String announcement) {
        this.announcement.set(announcement);
    }

    @Override
    public void notifyPlaySound(int _id){
        music.playSound(_id);
    }

    @Override
    public void notifyStageChanged(Action stage) {
        switch (stage){
            case 狼人:
                announcement.set("狼人請睜眼");
                music.playSound(R.raw.wolf_open);
                break;
            case 殺人:
                announcement.set("雙擊殺人");
                music.playSound(R.raw.wolf_kill);
                break;
            case 女巫:
                if(Static.dataModel.getTurn() != 1){
                    /**
                     * 不是第一個輪次時，女巫可能沒有藥，控制女巫可以點選的座位
                     */
                    int seat = Static.dataModel.getSeatRoleMap().get(stage);
                    if(Static.dataModel.getDieList().contains(seat)){
                        skipStage();
                        setAllTgBtnState(false);
                    }else{
                        stageDelay(stage);
                        if(!witch.hasPoison()){
                            setAllTgBtnState(false);
                            tgBtnGroup[seat].setClickable(true);
                            if(witch.hasHerbal()){
                                tgBtnGroup[wolves.getKnifeOn()].setClickable(true);
                            }else{
                                announcement.set("沒藥了點自己");
                            }
                        }
                    }
                }else{
                    stageDelay(stage);
                }
                music.playSound(R.raw.witch_open);
                break;
        }
    }
}
