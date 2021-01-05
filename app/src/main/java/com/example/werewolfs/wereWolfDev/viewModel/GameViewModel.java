package com.example.werewolfs.wereWolfDev.viewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.example.werewolfs.R;
import com.example.werewolfs.wereWolfDev.SoundMgr;
import com.example.werewolfs.wereWolfDev.activity.GameActivityNotify;
import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.EndType;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.model.DataModel;
import com.example.werewolfs.wereWolfDev.model.job.Bear;
import com.example.werewolfs.wereWolfDev.model.job.Guard;
import com.example.werewolfs.wereWolfDev.model.job.HiddenWolf;
import com.example.werewolfs.wereWolfDev.model.job.Hunter;
import com.example.werewolfs.wereWolfDev.model.job.Idiot;
import com.example.werewolfs.wereWolfDev.model.job.Knight;
import com.example.werewolfs.wereWolfDev.model.Role;
import com.example.werewolfs.wereWolfDev.model.job.Seer;
import com.example.werewolfs.wereWolfDev.model.job.Witch;
import com.example.werewolfs.wereWolfDev.model.job.Wolf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.decrementExact;
import static java.lang.Math.random;

public class GameViewModel extends AndroidViewModel implements GameNotify {

    private static final String TAG = "GameViewModel";
    private GameActivityNotify gameActivityNotify;

    /**
     * 儲存使用者行為 當前被選擇對象 上一次被選擇對象(用於單選)
     */
    private int selected, lastSelect;
    /**
     * 儲存使用行為 被選擇對象數量(用於多選)
     */
    private List<Integer> seatsSelected;

    /**
     * 身分與職業
     */
    private Wolf wolves = new Wolf();
    private Witch witch = new Witch();
    private Seer seer = new Seer();
    private Guard guard = new Guard();
    private Hunter hunter = new Hunter();
    private Bear bear = new Bear();
    private Knight knight = new Knight();
    private Idiot idiot = new Idiot();
    private HiddenWolf hiddenWolf = new HiddenWolf();

    /**
     * observable object
     */
    public static class CtrlBtnField {
        public final ObservableBoolean clickable = new ObservableBoolean();
        public final ObservableBoolean background = new ObservableBoolean(); //be white if true, black if false
        public final ObservableInt textColor = new ObservableInt();
        public final ObservableField<String> text = new ObservableField<>();

        CtrlBtnField(boolean clickable, boolean background, int textColor, String text) {
            this.clickable.set(clickable);
            this.background.set(background);
            this.textColor.set(textColor);
            this.text.set(text);
        }
    }

    /**
     * variable
     */
    private final Context mContext; //to avoid memory leaks, this must be an Application context
    private SoundMgr music;
    private ToggleButton[] tgBtnGroup;
    String message = "";

    /**
     * observable variable
     */
    public final ObservableField<String> announcement = new ObservableField<>();
    public final ObservableField<String> checkText = new ObservableField<>();
    public final ObservableBoolean checkVisible = new ObservableBoolean();
    public final CtrlBtnField ctrlBtnField = new CtrlBtnField(true, false, Color.WHITE, "夜晚");

    public GameViewModel(@NonNull Application app) {
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

        Static.dataModel.setGameNotify(this);
    }

    public void initSelect() {
        selected = 0;
        lastSelect = 0;
    }

    public void initGameVariable() {
        Static.dataModel.initGameVariable();
    }

    public void createSound() {
        music = new SoundMgr(mContext);
    }

    public void setTgBtn(ToggleButton[] tgBtnGroup) {
        this.tgBtnGroup = tgBtnGroup;
        for (int i = 1; i <= Static.dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setOnCheckedChangeListener(onSeatClick);
        }
    }

    /**
     * 中間按鈕點擊事件
     */
    public void onCenterClick() {
        DataModel dataModel = Static.dataModel;
        if (dataModel.isDay()) {
            music.playSound(R.raw.howling);
            dataModel.dayEnd();
        } else {
            switch (dataModel.getStage()) {
                case 守衛:
                    if(guard.getSeat() == 0) return; //避免守衛未確認身分按空守
                    guard.protect(0);
                    if (selected != 0) {
                        setPositionFalse(selected);
                    }
                    music.playSound(guard.closeSound);
                    break;
                case 禁言長老:
                    break;
                default:
            }
            initSelect();
            dataModel.setNextStage();
            ctrlBtnField.text.set("夜晚");
            ctrlBtnField.clickable.set(false);
        }
    }

    /**
     * 座位點擊事件
     */
    private CompoundButton.OnCheckedChangeListener onSeatClick = ((buttonView, isChecked) -> {
        int seat = findIndexByBtnArray(buttonView);
        Action stage = Static.dataModel.getStage();

        if (isChecked) {
            checkedEvent(seat, stage);
        } else {
            if (Static.dataModel.isDay()) {
                buttonView.setBackgroundColor(Color.WHITE);
            } else {
                buttonView.setBackgroundColor(Color.BLACK);
            }
            unCheckedEvent(seat, stage);
        }
    });

    private void checkedEvent(int seat, Action stage) {
        ToggleButton tgBtn = tgBtnGroup[seat];
        DataModel dataModel = Static.dataModel;
        HashMap<Action, Integer> seatRoleMap = dataModel.getGodRoleMap();
        switch (stage) {
            case 狼人:
                setColorAndTextOn(tgBtn, "狼人", Color.BLUE);
                seatsSelected.add(seat);
                if (seatsSelected.size() == dataModel.getWolfCnt()) {
                    showCheckBtn("狼人為 : " + seatsSelected + " \n確認是否為您的夥伴");
                }
                break;
            case 殺人:
                setColorAndTextOn(tgBtn, "擊殺", Color.RED);
                choosePosition(seat);
                break;
            case 女巫:
                choosePosition(seat);
                if (witch.unChecked()) {
                    setColorAndTextOn(tgBtn, "女巫", Color.BLUE);
                } else {
                    if (dataModel.isWitchSaveRule() && dataModel.getTurn() == 1) {
                        //女巫可以自救的情況
                        if (wolves.getKnifeOn() == seat && witch.hasHerbal()) {
                            setColorAndTextOn(tgBtn, "拯救?", Color.BLUE);
                        } else if (witch.getSeat() == selected) {
                            setColorAndTextOn(tgBtn, "不用藥?", Color.BLUE);
                        } else {
                            setColorAndTextOn(tgBtn, "毒殺?", Color.RED);
                        }
                    } else {
                        if (witch.getSeat() == selected) {
                            setColorAndTextOn(tgBtn, "不用藥?", Color.BLUE);
                        } else if (wolves.getKnifeOn() == selected && witch.hasHerbal()) {
                            setColorAndTextOn(tgBtn, "拯救?", Color.BLUE);
                        } else {
                            setColorAndTextOn(tgBtn, "毒殺?", Color.RED);
                        }
                    }
                }
                break;
            case 預言家:
                choosePosition(seat);
                if (seer.unChecked()) {
                    setColorAndTextOn(tgBtn, "預言家", Color.GRAY);
                } else {
                    setColorAndTextOn(tgBtn, "查驗", Color.BLUE);
                }
                break;
            case 守衛:
                choosePosition(seat);
                if (guard.unChecked()) {
                    setColorAndTextOn(tgBtn, "守衛", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "保護", Color.GRAY);
                }
                break;
            case 白天:
                choosePosition(seat);
                setColorAndTextOn(tgBtn, "確認", Color.GREEN);
                break;
            default:
                choosePosition(seat);
                setColorAndTextOn(tgBtn, stage + "", Color.BLUE);
        }
    }

    private void unCheckedEvent(int seat, Action stage) {

        if (stage != Action.狼人) {
            if(seat != selected){
                return;
            }
        }

        DataModel dataModel = Static.dataModel;
        HashMap<Action, Integer> seatRoleMap = dataModel.getGodRoleMap();
        switch (stage) {
            case 狼人:
                seatsSelected.remove(new Integer(seat));
                break;
            case 殺人:
                wolves.kill(seat);
                music.playSound(R.raw.wolf_close);
                dataModel.setNextStage();
                break;
            case 女巫:
                if (witch.getSeat() == 0) {
                    useYourSkill(witch, seat, true);
                    //第一輪尚未確定誰是女巫，確認後跳出被刀的對象
                    tgBtnGroup[wolves.getKnifeOn()].setBackgroundColor(Color.RED);
                } else {
                    //如果女巫沒有藥，在進入女巫stage不能用藥對象要被鎖定(notifyStageChanged)
                    if (witch.hasHerbal() && wolves.getKnifeOn() == seat) {
                        witch.useHerbal(seat);
                    } else if (witch.hasPoison() && seatRoleMap.get(stage) != seat) {
                        witch.usePoison(seat);
                    }
                    closeYourEyes(witch);
                    //女巫未點選拯救時，會需要更改被狼人刀的座位為原本的顏色
                    tgBtnGroup[wolves.getKnifeOn()].setBackgroundColor(Color.BLACK);
                }
                break;
            case 預言家:
                if (seer.getSeat() == 0) {
                    useYourSkill(seer, seat, true);
                } else {
                    boolean isWolf = seer.isWolf(seat);
                    gameActivityNotify.notifySeerAsk(isWolf, seat, seer);
                }
                break;
            case 守衛:
                if (guard.getSeat() == 0) {
                    useYourSkill(guard, seat, true);
                    ctrlBtnField.text.set("空守");
                    ctrlBtnField.clickable.set(true);
                } else {
                    //守衛不能同守 解除座位鎖定
                    if (guard.getIsProtected() != 0) {
                        tgBtnGroup[guard.getIsProtected()].setClickable(true);
                    }
                    guard.protect(seat);
                    ctrlBtnField.text.set("夜晚");
                    ctrlBtnField.clickable.set(false);
                    closeYourEyes(guard);
                }
                break;
            case 獵人:
                setSeat(hunter, seat, true);
                closeYourEyes(hunter);
                break;
            case 熊:
                setSeat(bear, seat, true);
                closeYourEyes(bear);
                break;
            case 騎士:
                setSeat(knight, seat, true);
                closeYourEyes(knight);
                break;
            case 白癡:
                setSeat(idiot, seat, true);
                closeYourEyes(idiot);
                break;
            case 隱狼:
                setSeat(hiddenWolf, seat, false);
                closeYourEyes(hiddenWolf);
                break;
            case 白天:
                gameActivityNotify.notifyVoteCheck(seat);
                break;
        }
        initSelect();
    }

    /**
     * 中央提示按鈕點擊事件
     */

    public void onYesClick() {
        DataModel dataModel = Static.dataModel;
        List<Integer> wolves = dataModel.getWolfGroup();
        wolves.addAll(seatsSelected);

        for (int seat : wolves) {
            setPositionFalse(seat);
        }
        checkVisible.set(false);
        Log.d(TAG, "wolves-> " + wolves);
        Static.dataModel.setNextStage();
    }

    public void onNoClick() {
        List<Integer> temp = new ArrayList<>();
        temp.addAll(seatsSelected);
        for (int seat : temp) {
            setPositionFalse(seat);
        }
        checkVisible.set(false);
    }


    /**
     * 處理玩家選擇行為顯示
     *
     * @param selected
     */
    public void choosePosition(int selected) {
        this.selected = selected;
        if (lastSelect != 0) {
            Log.d(TAG, "selected->" + this.selected + " last_select->" + lastSelect);
            setPositionFalse(lastSelect);
        }
        lastSelect = selected;
    }

    /**
     * 取消選取行為顯示內容
     *
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
     *
     * @param bool
     */
    private void setAllSeatState(boolean bool) {
        for (int i = 1; i <= Static.dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setClickable(bool);
        }
    }

    /**
     * 改變全部座位顏色
     */
    private void setAllTgBtnStyle() {
        int textColor, bgColor;
        if (Static.dataModel.isDay()) {
            textColor = Color.BLACK;
            bgColor = Color.WHITE;
        } else {
            textColor = Color.WHITE;
            bgColor = Color.BLACK;
        }

        for (int i = 1; i <= Static.dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setTextColor(textColor);
            tgBtnGroup[i].setBackgroundColor(bgColor);
        }
    }

    /**
     * 設置單個按鈕點開的顏色與文字
     *
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
     *
     * @param btn
     * @return
     */
    private int findIndexByBtnArray(CompoundButton btn) {
        for (int i = 0; i < tgBtnGroup.length; i++) {
            if (tgBtnGroup[i] == btn) return i;
        }
        return -1;
    }

    /**
     * 跳出中央選擇提示框
     *
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
                announcement.set("玩家死亡 ->");
                ctrlBtnField.text.set("點我跳過");
                ctrlBtnField.clickable.set(true);
            }
        }.start();
    }

    /**
     * 進入下一階段，開眼與閉眼間格時間，請玩家睜眼
     */
    public void stageDelay(Action stage, Role role) {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                announcement.set(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                announcement.set(stage + "請睜眼");
                music.playSound(role.openSound);
            }
        }.start();
    }

    /**
     * 檢查玩家確認後腳色是否重複
     *
     * @param seat
     */
    public void repeatSelectionCheck(int seat) {
        DataModel dataModel = Static.dataModel;
        if (dataModel.getGodRoleMap().values().contains(seat)
                || dataModel.getWolfRoleMap().values().contains(seat)) {
            gameActivityNotify.notifyRepeatSelect();
        } else {
            return;
        }
    }

    /**
     * 初始化按鈕點擊 讓死亡玩家改變顯示 不可被點擊
     */
    public void initSeatState() {
        setAllSeatState(true);
        for (int seat : Static.dataModel.getDieList()) {
            tgBtnGroup[seat].setClickable(false);
            tgBtnGroup[seat].setBackgroundColor(Color.RED);
            tgBtnGroup[seat].setText("DIE");
        }
    }

    private void openYourEyes(Role role) {
        announcement.set(role.stage + "請睜眼");
        music.playSound(role.openSound);
        if (Static.dataModel.getDieList().contains(role.getSeat())) {
            skipStage();
            setAllSeatState(false);
        } else {
            initSeatState(); // ? 必要? TODO
            String btnText = "";
            switch (role.stage) {
                case 守衛:
                    btnText = "空守";
                    ctrlBtnField.clickable.set(true);
                    break;
                default:
                    btnText = "夜晚";
            }
            ctrlBtnField.text.set(btnText);
        }
    }

    private void useYourSkill(Role role, int seat, boolean isGod) {
        repeatSelectionCheck(seat);
        Action stage = role.stage;
        announcement.set(stage + "請使用技能");
        music.playSound(role.skillSound);
        if(isGod){
            Static.dataModel.getGodRoleMap().put(stage, seat);
        }else{
            Static.dataModel.getWolfRoleMap().put(stage, seat);
        }
        role.setSeat(seat);
        Log.d(TAG, stage + "為 : " + seat + "號玩家");
    }

    private void setSeat(Role role, int seat, boolean isGod){
        repeatSelectionCheck(seat);
        Action stage = role.stage;
        if(isGod){
            Static.dataModel.getGodRoleMap().put(stage, seat);
        }else{
            Static.dataModel.getWolfRoleMap().put(stage, seat);
        }
        role.setSeat(seat);
        initSelect();
        Log.d(TAG, stage + "為 : " + seat + "號玩家");
    }

    public void closeYourEyes(Role role) {
        music.cancelSound();
        music.playSound(role.closeSound);
        Static.dataModel.setNextStage();
    }

    /**
     * 第一天獲取發言順序(警長競選)
     * @return
     */
    private int getSpeakOrder(){
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        Date now = new Date(System.currentTimeMillis());
        int min = Integer.parseInt(sdf.format(now));
        int speakOrder = (min % (Static.dataModel.getPeoCnt())) + 1;
        if(random() < 0.5){
            speakOrder = -speakOrder;
        }
        return speakOrder;
    }

    /**
     * 根據昨晚死亡玩家選定對話文字
     * @param dieList
     * @return
     */
    public String checkDieList(List<Integer> dieList){
        String text="";
        if(dieList.size() > 0){
            text = "昨晚死亡玩家為 : " + dieList;
        }else{
            int speakOrder = getSpeakOrder();
            String str;
            if(speakOrder > 0){
                str = "順序";
            }else{
                str = "逆序";
                speakOrder = -speakOrder;
            }
            text = "昨晚為平安夜\n根據現在時間由" + speakOrder + "號玩家\"" + str + "\"開始發言";
        }
        return text;
    }

    /** 白天投票*/
    public void voteOn(int seat){
        Static.dataModel.playerDead(seat);
        initSeatState();
    }

    /** 計時後天亮*/
    private void dayBreak(List<Integer> dieList_today, int turn){
        music.playSound(R.raw.daybreak);
        announcement.set("點此進入下一晚->");
        ctrlBtnField.background.set(false);
        ctrlBtnField.text.set("第" + turn + "天");
        ctrlBtnField.textColor.set(Color.WHITE);
        ctrlBtnField.clickable.set(true);
        setAllTgBtnStyle();
        if(turn == 1){
            gameActivityNotify.notifyFirstDaybreak(message, dieList_today);
        }else {
            gameActivityNotify.notifyDaybreak(message);
            initSeatState();
        }
    }

    /** 熊吼叫邏輯*/
    private boolean bearRoar(){
        DataModel dataModel = Static.dataModel;
        int seat = bear.getSeat();
        int peo_cnt = dataModel.getPeoCnt();
        List<Integer> dieList = dataModel.getDieList();
        List<Integer> wolfGroup = dataModel.getWolfGroup();
        int right = seat, left = seat;
        do{
            left = (left - 1) % peo_cnt;
            if(left == 0) { left = peo_cnt; }
        }while(dieList.contains(left));

        if(wolfGroup.contains(left)){
            return true;
        }
        do{
            right = (right + 1) % peo_cnt;
            if(right == 0) { right = 1; }
        }while(dieList.contains(right));

        if(wolfGroup.contains(right)){
            return true;
        }else{
            return false;
        }
    }




    @Override
    public void notifyStageChanged(Action stage) {
        DataModel dataModel = Static.dataModel;
        switch (stage) {
            case 狼人:
                announcement.set("狼人請睜眼");
                music.playSound(R.raw.wolf_open);
                if(dataModel.getTurn() != 1){
                    //第一輪之後狼人不用確認身分 播放聲音後跳到殺人
                    dataModel.setNextStage();
                }
                break;
            case 殺人:
                announcement.set("雙擊殺人");
                music.playSound(R.raw.wolf_kill);
                break;
            case 女巫:
                if (Static.dataModel.getTurn() != 1) {
                    /**
                     * 不是第一個輪次時，女巫可能沒有藥，控制女巫可以點選的座位
                     */
                    int seat = witch.getSeat();
                    if (dataModel.getDieList().contains(seat)) {
                        music.playSound(witch.openSound);
                        skipStage();
                        setAllSeatState(false);
                    } else {
                        stageDelay(stage, witch);
                        if (!witch.hasPoison()) {
                            setAllSeatState(false);
                            tgBtnGroup[seat].setClickable(true);
                            if (witch.hasHerbal()) {
                                tgBtnGroup[wolves.getKnifeOn()].setClickable(true);
                            } else {
                                announcement.set("沒藥了點自己");
                            }
                        }
                    }
                } else {
                    stageDelay(stage, witch);
                }break;
            case 預言家:
                openYourEyes(seer);
                break;
            case 守衛:
                openYourEyes(guard);
                if(dataModel.getTurn() != 1){
                    ctrlBtnField.text.set("空守");
                    ctrlBtnField.clickable.set(true);
                }
                //守衛不能同守 鎖定座位
                if (guard.getIsProtected() != 0) {
                    tgBtnGroup[guard.getIsProtected()].setClickable(false);
                }break;
            case 獵人:
                openYourEyes(hunter);
                break;
            case 熊:
                openYourEyes(bear);
                break;
            case 騎士:
                openYourEyes(knight);
                break;
            case 白癡:
                openYourEyes(idiot);
                break;
            case 隱狼:
                openYourEyes(hiddenWolf);
                gameActivityNotify.notifyWolfFriend(Static.dataModel.getWolfGroup());
                break;
            case 白天:
                dataModel.nightEnd();
                break;

        }
    }

    @Override
    public void notifyDayEnd() {
        DataModel dataModel = Static.dataModel;
        ctrlBtnField.textColor.set(Color.BLACK);
        ctrlBtnField.background.set(true);
        ctrlBtnField.clickable.set(false);
        setAllSeatState(true);
        setAllTgBtnStyle();
        dataModel.setNextStage();
        initSeatState();
    }

    @Override
    public void notifyNightEnd(){
        message = "";
        List<Integer> dieList_today = new ArrayList<>(); //存放當晚死亡玩家 用於文字顯示
        List<Integer> dieList = Static.dataModel.getDieList();
        int turn = Static.dataModel.getTurn();

        /**
         * 目前玩家會死亡的狀況
         * 1.被狼人刀 -> 女巫不救 且 守衛非守
         * 2.被狼人刀 -> 被女巫救 -> 被守衛守
         * 3.被女巫毒
         */
        int knifeOn = wolves.getKnifeOn();
        if((knifeOn != witch.getIsSave())
         || witch.getIsSave() == guard.getIsProtected()
         && knifeOn != guard.getIsProtected()){
            Static.dataModel.playerDead(knifeOn);
            dieList_today.add(knifeOn);
            witch.setIsSave(0);
        }

        int isPoisoned = witch.getIsPoisoned();
        if(isPoisoned != 0){
            //女巫有投毒
            if(!dieList_today.contains(isPoisoned)){
                //被毒的人未死，加入死亡名單
                Static.dataModel.playerDead(isPoisoned);
                dieList_today.add(isPoisoned);
            }
            witch.setIsPoisoned(0);
        }
        Collections.sort(dieList_today);
        Log.d(TAG, "dieList(today) -> " + dieList_today);

        if(turn == 1){
            int speakOrder = getSpeakOrder();
            String str;
            if(speakOrder > 0){
                str = "順序";
            }else{
                str = "逆序";
                speakOrder = -speakOrder;
            }
            message = "競選警長階段\n根據現在時間由" + speakOrder + "號玩家\"" + str + "\"開始發言";
        }else{
            message = checkDieList(dieList_today);
        }

        int bearSeat = bear.getSeat();
        if(bearSeat != 0 && bear.isAlive){
            boolean roar = bearRoar();
            if(roar){
                music.playSound(bear.skillSound);
                message += "\n熊叫了！(╬ﾟдﾟ)";
            }else{
                message += "\n熊沒有叫( ˘•ω•˘ )！";
            }
            if(dieList_today.contains(bearSeat)){
                message += "\n熊不再吼叫(´;ω;`)";
                bear.killed();
            }
        }

        new CountDownTimer(5000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                announcement.set(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                dayBreak(dieList_today, turn);
            }
        }.start();
        Static.dataModel.nextTurn();
    }

    @Override
    public void notifyGameEnd(EndType endType) {
        Log.d(TAG, "遊戲結束");
        String endText = "遊戲結束 " + endType;
        String endMessage = "";
        HashMap<Action, Integer> godRoleMap = Static.dataModel.getGodRoleMap();
        for(Action act : godRoleMap.keySet()){
            endMessage += "\n" + godRoleMap.get(act) + " . " + act;
        }
        gameActivityNotify.notifyGameEnd(endText, endMessage);
    }

    @Override
    public void notifyShowHiddenWolf() {
        int seat = hiddenWolf.getSeat();
        if(seat != 0){
            Static.dataModel.getWolfGroup().add(seat);
        }
    }
}
