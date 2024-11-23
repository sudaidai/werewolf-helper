package com.wf.werewolfs.android.viewModel;

import static java.lang.Math.random;

import android.app.Application;
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

import com.wf.werewolfs.R;
import com.wf.werewolfs.android.SoundMgr;
import com.wf.werewolfs.android.activity.GameActivityNotify;
import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.constant.EndType;
import com.wf.werewolfs.android.model.DataModel;
import com.wf.werewolfs.android.model.Role;
import com.wf.werewolfs.android.model.job.Bear;
import com.wf.werewolfs.android.model.job.ForbiddenElder;
import com.wf.werewolfs.android.model.job.Gargoyle;
import com.wf.werewolfs.android.model.job.Guard;
import com.wf.werewolfs.android.model.job.HiddenWolf;
import com.wf.werewolfs.android.model.job.Hunter;
import com.wf.werewolfs.android.model.job.Idiot;
import com.wf.werewolfs.android.model.job.Knight;
import com.wf.werewolfs.android.model.job.PrettyWolf;
import com.wf.werewolfs.android.model.job.Seer;
import com.wf.werewolfs.android.model.job.Shaman;
import com.wf.werewolfs.android.model.job.TombKeeper;
import com.wf.werewolfs.android.model.job.Witch;
import com.wf.werewolfs.android.model.job.Wolf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GameViewModel extends AndroidViewModel implements GameNotify {

    private static final String TAG = "GameViewModel";
    private GameActivityNotify gameActivityNotify;
    private final DataModel dataModel = DataModel.getInstance();

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
    private final Wolf wolves = new Wolf();
    private final Witch witch = new Witch();
    private final Seer seer = new Seer();
    private final Guard guard = new Guard();
    private final Hunter hunter = new Hunter();
    private final Bear bear = new Bear();
    private final Knight knight = new Knight();
    private final Idiot idiot = new Idiot();
    private final HiddenWolf hiddenWolf = new HiddenWolf();
    private final ForbiddenElder forbiddenElder = new ForbiddenElder();
    private final PrettyWolf prettyWolf = new PrettyWolf();
    private final Gargoyle gargoyle = new Gargoyle();
    private final Shaman shaman = new Shaman();
    private final TombKeeper tombKeeper = new TombKeeper();
//    private final Magician magician = new Magician();

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
    private SoundMgr music;
    private ToggleButton[] tgBtnGroup;
    String message = "";
    private boolean knifeOnTheGround;

    /**
     * observable variable
     */
    public final ObservableField<String> announcement = new ObservableField<>();
    public final ObservableField<String> checkText = new ObservableField<>();
    public final ObservableBoolean checkVisible = new ObservableBoolean();
    public final CtrlBtnField ctrlBtnField = new CtrlBtnField(true, false, Color.WHITE, "夜晚");

    public GameViewModel(@NonNull Application app) {
        super(app);
        init();
    }

    public void setGameActivityNotify(GameActivityNotify gameActivityNotify) {
        this.gameActivityNotify = gameActivityNotify;
    }

    private void init() {
        announcement.set("準備開始");
        initSelect();
        seatsSelected = new ArrayList<>();
        knifeOnTheGround = false;
        dataModel.setGameNotify(this);
    }

    public void initSelect() {
        selected = 0;
        lastSelect = 0;
    }

    public void initGameVariable() {
        dataModel.initGameVariable();
    }

    public void createSound() {
        music = SoundMgr.getInstance(getApplication().getApplicationContext());
    }

    public void setTgBtn(ToggleButton[] tgBtnGroup) {
        this.tgBtnGroup = tgBtnGroup;
        for (int i = 1; i <= dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setOnCheckedChangeListener(onSeatClick);
        }
    }

    /**
     * 中間按鈕點擊事件
     */
    public void onCenterClick() {
        ctrlBtnField.clickable.set(false);

        // 如果點擊中間按鈕前有點擊座位(顏色不同) 在進入下一個stage之前重置該座位顏色與文字
        if (selected != 0) {
            setPositionFalse(selected);
        }

        if (dataModel.isDay()) {
            music.playSound(R.raw.howling);
            dataModel.dayEnd();
        } else {
            Role actRole = null;
            switch (dataModel.getStage()) {
                case 女巫:
                    actRole = witch;
                    break;
                case 預言家:
                    actRole = seer;
                    break;
                case 守衛:
                    actRole = guard;
                    if (guard.unChecked()) {
                        //避免守衛未確認身分按空守
                        return;
                    }
                    guard.protect(0);
                    break;
                case 禁言長老:
                    actRole = forbiddenElder;
                    forbiddenElder.mute(0);
                    break;
                case 狼美人:
                    actRole = prettyWolf;
                    prettyWolf.charm(0);
                    initSeatState();
                    break;
                case 獵人:
                    actRole = hunter;
                    if (!hunter.unChecked()) {
                        hunter.shoot(0);
                    } else {
                        ctrlBtnField.clickable.set(true);
                    }
                default:
            }
            initSelect();
            ctrlBtnField.text.set("夜晚");
            if (actRole != null && !(actRole == hunter && hunter.unChecked())) {
                closeYourEyes(actRole);
            }
        }
    }

    /**
     * 座位點擊事件
     */
    private final CompoundButton.OnCheckedChangeListener onSeatClick = ((buttonView, isChecked) -> {
        int seat = findIndexByBtnArray(buttonView);
        Action stage = dataModel.getStage();

        if (isChecked) {
            checkedEvent(seat, stage);
        } else {
            if (dataModel.isDay()) {
                buttonView.setBackgroundColor(Color.WHITE);
            } else {
                buttonView.setBackgroundColor(Color.BLACK);
            }
            unCheckedEvent(seat, stage);
        }
    });

    private void checkedEvent(int seat, Action stage) {
        ToggleButton tgBtn = tgBtnGroup[seat];
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
                    if (!dataModel.isWitchSaveRule() && dataModel.isFirstDay()) {
                        //女巫可以自救的情況
                        if (wolves.getKnifeOn() == seat && witch.hasHerbal()) {
                            setColorAndTextOn(tgBtn, "拯救?", Color.BLUE);
                        } else if (witch.getSeat() == selected) {
                            setColorAndTextOn(tgBtn, "不用藥", Color.BLUE);
                        } else {
                            setColorAndTextOn(tgBtn, "毒殺?", Color.RED);
                        }
                    } else {
                        if (witch.getSeat() == selected) {
                            setColorAndTextOn(tgBtn, "不用藥", Color.BLUE);
                        } else if (wolves.getKnifeOn() == seat && witch.hasHerbal()) {
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
                    setColorAndTextOn(tgBtn, "預言家", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "查驗", Color.GRAY);
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
            case 禁言長老:
                choosePosition(seat);
                if (forbiddenElder.unChecked()) {
                    setColorAndTextOn(tgBtn, "禁言長老", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "禁言", Color.GRAY);
                }
                break;
            case 狼美人:
                choosePosition(seat);
                if (prettyWolf.unChecked()) {
                    setColorAndTextOn(tgBtn, "狼美人", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "魅惑", Color.MAGENTA);
                }
                break;
            case 石像鬼2:
                choosePosition(seat);
                if (knifeOnTheGround) {
                    setColorAndTextOn(tgBtn, "擊殺", Color.RED);
                } else {
                    setColorAndTextOn(tgBtn, "看透", Color.GRAY);
                }
                break;
            case 通靈師:
                choosePosition(seat);
                if (shaman.unChecked()) {
                    setColorAndTextOn(tgBtn, "通靈師", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "看透", Color.GRAY);
                }
                break;
            case 獵人:
                choosePosition(seat);
                if (hunter.unChecked()) {
                    setColorAndTextOn(tgBtn, "獵人", Color.BLUE);
                } else {
                    setColorAndTextOn(tgBtn, "開槍", Color.RED);
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
            if (seat != selected) {
                return;
            }
        }

        HashMap<Action, Integer> seatRoleMap = dataModel.getGodRoleMap();
        Action identity;
        switch (stage) {
            case 狼人:
                seatsSelected.remove(Integer.valueOf(seat));
                break;
            case 殺人:
                if (!dataModel.isFirstDay()) {
                    tgBtnGroup[wolves.getKnifeOn()].setClickable(true); //同刀的座位鎖定解除
                }
                wolves.kill(seat);
                music.playSound(R.raw.wolf_close);
                dataModel.setNextStage();
                break;
            case 女巫:
                if (witch.unChecked()) {
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
                if (seer.unChecked()) {
                    useYourSkill(seer, seat, true);
                } else {
                    boolean isWolf = seer.see(seat);
                    gameActivityNotify.notifySeerAsk(isWolf, seat, seer);
                }
                break;
            case 守衛:
                if (guard.unChecked()) {
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
                if (hunter.unChecked()) {
                    setSeat(hunter, seat, true);
                    hunterCheck();
                } else {
                    hunter.shoot(seat);
                    ctrlBtnField.text.set("夜晚");
                    ctrlBtnField.clickable.set(false);
                    closeYourEyes(hunter);
                }
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
            case 禁言長老:
                if (forbiddenElder.unChecked()) {
                    useYourSkill(forbiddenElder, seat, true);
                    ctrlBtnField.text.set("空禁");
                    ctrlBtnField.clickable.set(true);
                } else {
                    forbiddenElder.mute(seat);
                    ctrlBtnField.text.set("夜晚");
                    ctrlBtnField.clickable.set(false);
                    closeYourEyes(forbiddenElder);
                }
                break;
            case 狼美人:
                if (prettyWolf.unChecked()) {
                    useYourSkill(prettyWolf, seat, false);
                    ctrlBtnField.text.set("空綁");
                    ctrlBtnField.clickable.set(true);
                } else {
                    prettyWolf.charm(seat);
                    ctrlBtnField.text.set("夜晚");
                    ctrlBtnField.clickable.set(false);
                    closeYourEyes(prettyWolf);
                }
                break;
            case 石像鬼:
                setSeat(gargoyle, seat, false);
                dataModel.getWolfGroup().add(seat); //為預言家查殺對象
                closeYourEyes(gargoyle);
                break;
            case 石像鬼2:
                if (knifeOnTheGround) {
                    wolves.kill(seat);
                    dataModel.setNextStage();
                } else {
                    identity = gargoyle.seeThrough(seat);
                    gameActivityNotify.notifySeeThrough(identity, seat, gargoyle);
                }
                break;
            case 通靈師:
                if (shaman.unChecked()) {
                    useYourSkill(shaman, seat, true);
                } else {
                    identity = shaman.seeThrough(seat);
                    gameActivityNotify.notifySeeThrough(identity, seat, shaman);
                }
                break;
            case 守墓人:
                setSeat(tombKeeper, seat, true);
                closeYourEyes(tombKeeper);
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
        List<Integer> wolves = dataModel.getWolfGroup();
        wolves.addAll(seatsSelected);

        for (int seat : wolves) {
            setPositionFalse(seat);
        }
        checkVisible.set(false);
        Log.d(TAG, "wolves-> " + wolves);
        dataModel.setNextStage();
    }

    public void onNoClick() {
        List<Integer> temp = new ArrayList<>();
        temp.addAll(seatsSelected);
        for (int seat : temp) {
            setPositionFalse(seat);
        }

        seatsSelected.clear();
        checkVisible.set(false);
    }


    /**
     * 處理玩家選擇行為顯示
     *
     * @param selected 玩家點擊座位
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
     * @param pos 應該取消的座位
     */
    public void setPositionFalse(int pos) {
        tgBtnGroup[pos].setOnCheckedChangeListener(null);

        tgBtnGroup[pos].setChecked(false);
        if (dataModel.isDay()) {
            tgBtnGroup[pos].setBackgroundColor(Color.WHITE);
        } else {
            tgBtnGroup[pos].setBackgroundColor(Color.BLACK);
        }
        tgBtnGroup[pos].setText(String.valueOf(pos));
        tgBtnGroup[pos].setOnCheckedChangeListener(onSeatClick);
    }

    /**
     * 改變全部座位可否被點擊
     *
     * @param bool true 全部可選 false相反
     */
    public void setAllSeatState(boolean bool) {
        Log.d(TAG, "Set up seat state to " + bool);
        for (int i = 1; i <= dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setClickable(bool);
        }
    }

    /**
     * 改變全部座位顏色
     */
    private void setAllTgBtnStyle() {
        int textColor, bgColor;
        if (dataModel.isDay()) {
            textColor = Color.BLACK;
            bgColor = Color.WHITE;
        } else {
            textColor = Color.WHITE;
            bgColor = Color.BLACK;
        }

        for (int i = 1; i <= dataModel.getPeoCnt(); i++) {
            tgBtnGroup[i].setTextColor(textColor);
            tgBtnGroup[i].setBackgroundColor(bgColor);
        }
    }

    /**
     * 設置單個按鈕點開的顏色與文字
     *
     * @param tgBtn 單個toggle button 物件
     * @param text  btn 文字
     * @param color btn 顏色
     */
    public void setColorAndTextOn(ToggleButton tgBtn, String text, int color) {
        tgBtn.setTextOn(text);
        tgBtn.setBackgroundColor(color);
    }

    /**
     * 從按鈕群找到按鈕的index
     *
     * @param btn 獲取某個Button的索引
     * @return 索引
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
     * @param checkText 提視窗文字
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
        new CountDownTimer(6000, 1000) {
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
     * @param seat 正在設置角色的座位
     */
    public void repeatSelectionCheck(int seat) {
        if (dataModel.getGodRoleMap().containsValue(seat)
                || dataModel.getWolfRoleMap().containsValue(seat)) {
            gameActivityNotify.notifyRepeatSelect();
        }
    }

    /**
     * 初始化按鈕點擊 讓死亡玩家改變顯示 不可被點擊
     */
    public void initSeatState() {
        setAllSeatState(true);
        for (int seat : dataModel.getDieList()) {
            tgBtnGroup[seat].setClickable(false);
            tgBtnGroup[seat].setBackgroundColor(Color.RED);
            tgBtnGroup[seat].setText("DIE");
        }
    }

    private void openYourEyes(Role role) {
        announcement.set(role.stage + "請睜眼");
        music.playSound(role.openSound);
        if (dataModel.getDieList().contains(role.getSeat())) {
            skipStage();
            setAllSeatState(false);
        } else {
            initSeatState(); // ? 必要? TODO
        }
    }

    private void useYourSkill(Role role, int seat, boolean isGod) {
        repeatSelectionCheck(seat);
        Action stage = role.stage;
        announcement.set(stage + "請使用技能");
        music.playSound(role.skillSound);
        if (isGod) {
            dataModel.getGodRoleMap().put(stage, seat);
        } else {
            dataModel.getWolfRoleMap().put(stage, seat);
        }
        role.setSeat(seat);
        Log.d(TAG, stage + "為 : " + seat + "號玩家");
    }

    private void setSeat(Role role, int seat, boolean isGod) {
        repeatSelectionCheck(seat);
        Action stage = role.stage;
        if (isGod) {
            dataModel.getGodRoleMap().put(stage, seat);
        } else {
            dataModel.getWolfRoleMap().put(stage, seat);
        }
        role.setSeat(seat);
        initSelect();
        Log.d(TAG, stage + "為 : " + seat + "號玩家");
    }

    public void closeYourEyes(Role role) {
        music.cancelSound();
        music.playSound(role.closeSound);
        dataModel.setNextStage();
    }

    /**
     * 每天晚上，石像鬼都想要跟上帝要刀子
     */
    public void gargoyleGetKnife() {
        if (dataModel.wolvesDead()) {
            // 刀子在地上 石像鬼撿起來
            knifeOnTheGround = true;
            announcement.set("雙擊殺人");
        } else {
            //上帝不給他 叫他閉上眼滾開
            closeYourEyes(gargoyle);
        }
    }

    /**
     * 第一天獲取發言順序(警長競選)
     *
     * @return 發言順序首位
     */
    private int getSpeakOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        Date now = new Date(System.currentTimeMillis());
        int min = Integer.parseInt(sdf.format(now));
        int speakOrder = (min % (dataModel.getPeoCnt())) + 1;
        if (random() < 0.5) {
            speakOrder = -speakOrder;
        }
        return speakOrder;
    }

    /**
     * 根據昨晚死亡玩家選定對話文字
     *
     * @param dieList 今日死亡玩家座位
     * @return 拼裝後白天顯示字串
     */
    public String checkDieList(List<Integer> dieList) {
        String text;
        if (dieList.size() > 0) {
            text = "昨晚死亡玩家為 : " + dieList;
        } else {
            int speakOrder = getSpeakOrder();
            String str;
            if (speakOrder > 0) {
                str = "順序";
            } else {
                str = "逆序";
                speakOrder = -speakOrder;
            }
            text = "昨晚為平安夜\n根據現在時間由" + speakOrder + "號玩家\"" + str + "\"開始發言";
        }
        return text;
    }

    /**
     * 白天投票
     */
    public void voteOn(int seat) {
        dataModel.playerDead(seat);
        if (seat == prettyWolf.getSeat()) {
            //如果投了狼美人出去
            int lover = prettyWolf.getLover();
            gameActivityNotify.notifyPrettyWolfDead(lover);
            dataModel.playerDead(lover);
        }
        if (!tombKeeper.unChecked()) {
            tombKeeper.bury(seat);
        }
        initSeatState();
    }

    /**
     * 計時後天亮
     */
    private void dayBreak(List<Integer> dieList_today, int turn) {
        music.playSound(R.raw.daybreak);
        announcement.set("點此進入下一晚->");
        ctrlBtnField.background.set(false);
        ctrlBtnField.text.set("第" + turn + "天");
        ctrlBtnField.textColor.set(Color.WHITE);
        ctrlBtnField.clickable.set(true);
        setAllTgBtnStyle();

        if (!forbiddenElder.unChecked()) {
            int muted = forbiddenElder.getMuted();
            if (muted != 0) {
                tgBtnGroup[muted].setText("禁言");
            }
        }

        if (turn == 1) {
            gameActivityNotify.notifyFirstDaybreak(message, dieList_today);
        } else {
            gameActivityNotify.notifyDaybreak(message);
            initSeatState();
        }
    }

    @Override
    public void notifyStageChanged(Action stage) {
        switch (stage) {
            case 狼人:
                announcement.set("狼人請睜眼");
                music.playSound(R.raw.wolf_open);
                if (dataModel.hasStage(Action.狼美人)) {
                    //如果有狼美人 請狼人睜眼之後 也請狼美人睜眼 -> stage 為 Action.狼美人
                    announcement.set(Action.狼美人 + "請睜眼");
                    music.playSound(prettyWolf.openSound);
                }
                if (!dataModel.isFirstDay()) {
                    //第一輪之後狼人不用確認身分 播放聲音後跳到殺人
                    dataModel.setNextStage();
                }
                break;
            case 殺人:
                music.playSound(R.raw.wolf_kill);
                if (!dataModel.isFirstDay() && dataModel.getPeoCnt() <= 9) {
                    tgBtnGroup[wolves.getKnifeOn()].setClickable(false); // 9人以下不能同刀
                }
                if (dataModel.wolvesDead()) {
                    Log.d(TAG, "Knife on the ground.");
                    setAllSeatState(false);
                    skipStage();
                } else {
                    announcement.set("雙擊殺人");
                }
                break;
            case 女巫:
                if (!dataModel.isFirstDay()) {
                    // 不是第一個輪次時，女巫可能沒有藥，控制女巫可以點選的座位
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
                        }

                        if (witch.hasHerbal()) {
                            int knifeOn = wolves.getKnifeOn();
                            tgBtnGroup[knifeOn].setClickable(true);
                            tgBtnGroup[knifeOn].setBackgroundColor(Color.RED);
                        }

                        if (!witch.hasMedicine()) {
                            announcement.set("沒藥了點自己");
                        }
                    }
                } else {
                    stageDelay(stage, witch);
                }
                break;
            case 預言家:
                openYourEyes(seer);
                break;
            case 守衛:
                openYourEyes(guard);
                if (!dataModel.isFirstDay()) {
                    ctrlBtnField.text.set("空守");
                    ctrlBtnField.clickable.set(true);
                }
                //守衛不能同守 鎖定座位
                if (guard.getIsProtected() != 0) {
                    tgBtnGroup[guard.getIsProtected()].setClickable(false);
                }
                break;
            case 獵人:
                openYourEyes(hunter);
                ctrlBtnField.clickable.set(true);
                if (!dataModel.isFirstDay()) {
                    hunterCheck();
                }
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
                gameActivityNotify.notifyWolfFriend(dataModel.getWolfGroup());
                break;
            case 禁言長老:
                if (!dataModel.isFirstDay()) {
                    ctrlBtnField.text.set("空禁");
                    ctrlBtnField.clickable.set(true);
                }
                openYourEyes(forbiddenElder);
                break;
            case 狼美人:
                // 此時狼人閉眼，階段變為狼美人，請狼美人使用技能，如果是第一輪狼美人確認身分後使用技能
                if (dataModel.getDieList().contains(prettyWolf.getSeat())) {
                    skipStage();
                    setAllSeatState(false);
                } else if (!dataModel.isFirstDay()) {
                    music.playSound(prettyWolf.skillSound);
                    ctrlBtnField.clickable.set(true);
                    ctrlBtnField.text.set("空綁");
                }
                break;
            case 石像鬼:
                //只有第一輪確認身分時會用到 睜眼確認身分
            case 石像鬼2:
                //睜眼看某個人身分
                openYourEyes(gargoyle);
                break;
            case 通靈師:
                openYourEyes(shaman);
                break;
            case 守墓人:
                openYourEyes(tombKeeper);
                if (!dataModel.isFirstDay()) {
                    setAllSeatState(false);
                    gameActivityNotify.notifyInGrave(tombKeeper.identify(), tombKeeper);
                }
                break;
            case 白天:
                dataModel.nightEnd();
                break;

        }
    }

    @Override
    public void notifyDayEnd() {
        ctrlBtnField.textColor.set(Color.BLACK);
        ctrlBtnField.background.set(true);
        ctrlBtnField.clickable.set(false);
        setAllSeatState(true);
        setAllTgBtnStyle();
        if (!forbiddenElder.unChecked()) {
            int muted = forbiddenElder.getMuted();
            if (forbiddenElder.getMuted() != 0) {
                tgBtnGroup[muted].setText(String.valueOf(muted));
                forbiddenElder.setMuted(0);
            }
        }

        initSeatState();
        dataModel.setNextStage();
    }

    @Override
    public void notifyNightEnd() {
        message = "";
        List<Integer> dieListToday = new ArrayList<>(); //存放當晚死亡玩家 用於文字顯示
        List<Integer> dieList = dataModel.getDieList();
        int turn = dataModel.getTurn();

        // 目前玩家會死亡的狀況
        // 1.被狼人刀 -> 女巫不救 且 守衛非守
        // 2.被狼人刀 -> 被女巫救 -> 被守衛守
        // 3.被女巫毒

        int knifeOn = wolves.getKnifeOn();

        boolean knifeOnGone =
                (knifeOn == witch.getIsSave() && knifeOn == guard.getIsProtected()) ||
                (knifeOn != witch.getIsSave() && knifeOn != guard.getIsProtected());

        if (knifeOnGone) {
            dataModel.playerDead(knifeOn);
            dieListToday.add(knifeOn);
        }

        int isPoisoned = witch.getIsPoisoned();
        if (isPoisoned != 0) {
            // 3.被女巫毒
            if (!dieListToday.contains(isPoisoned)) {
                //被毒的人未死，加入死亡名單
                dataModel.playerDead(isPoisoned);
                dieListToday.add(isPoisoned);
            }
            witch.setIsPoisoned(0);
        }

        int bulletOn = hunter.getBulletOn();
        if (!hunter.isAlive && bulletOn != 0) {
            if (!dieListToday.contains(bulletOn)) {
                dataModel.playerDead(bulletOn);
                dieListToday.add(bulletOn);
            }
            hunter.shoot(0);
        }

        if (witch.getIsSave() != 0) {
            witch.setIsSave(0);
        }

        Collections.sort(dieListToday);
        Log.d(TAG, "dieList(today) -> " + dieListToday);

        if (turn == 1) {
            int speakOrder = getSpeakOrder();
            String str;
            if (speakOrder > 0) {
                str = "順序";
            } else {
                str = "逆序";
                speakOrder = -speakOrder;
            }
            message = "競選警長階段\n根據現在時間由" + speakOrder + "號玩家\"" + str + "\"開始發言";
        } else {
            message = checkDieList(dieListToday);
        }

        if (!bear.unChecked() && bear.isAlive) {
            boolean roar = bear.roar();
            if (roar) {
                music.playSound(bear.skillSound);
                message += ", 熊叫了！(╬ﾟдﾟ)";
            } else {
                message += ", 熊沒有叫( ˘•ω•˘ )！";
            }
            if (dieList.contains(bear.getSeat())) {
                message += ", 熊不再吼叫(´;ω;`)";
                bear.killed();
            }
        }

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                announcement.set(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                dayBreak(dieListToday, turn);
            }
        }.start();
        dataModel.nextTurn();
    }

    @Override
    public void notifyGameEnd(EndType endType) {
        Log.d(TAG, "遊戲結束");
        String endText = "遊戲結束 " + endType;
        StringBuilder sb = new StringBuilder();
        List<Action> actionList = dataModel.getActionList();
        for (int i = 1; i < actionList.size(); i++) {
            String seatRole = "座位" + i + ": " + actionList.get(i) + "\n";
            sb.append(seatRole);
        }

        // 不需要DataModel的資源了 初始化
        dataModel.initGameVariable();
        gameActivityNotify.notifyGameEnd(endText, sb.toString());
    }

    @Override
    public void notifyShowHiddenWolf() {
        if (!hiddenWolf.unChecked()) {
            dataModel.getWolfRoleMap().remove(Action.隱狼);
            dataModel.getWolfGroup().add(hiddenWolf.getSeat());
        }
    }

    public void hunterCheck() {
        int hunterSeat = hunter.getSeat();
        if (hunterSeat == wolves.getKnifeOn() && witch.getIsPoisoned() != hunterSeat &&
                ((hunterSeat == witch.getIsSave() && hunterSeat == guard.getIsProtected()) ||
                        (hunterSeat != witch.getIsSave() && hunterSeat != guard.getIsProtected()))) {
            ctrlBtnField.text.set("悶槍");
            hunter.killed();
            initSeatState();
        } else {
            if (!dataModel.isFirstDay()) {
                setAllSeatState(false);
                ctrlBtnField.text.set("點我跳過");
            } else {
                closeYourEyes(hunter);
            }
        }
    }
}
