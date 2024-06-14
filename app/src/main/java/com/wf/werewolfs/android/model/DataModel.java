package com.wf.werewolfs.android.model;

import android.util.Log;

import com.wf.werewolfs.android.constant.Action;
import com.wf.werewolfs.android.constant.EndType;
import com.wf.werewolfs.android.viewModel.GameNotify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataModel {

    private static final String TAG = "DataModel";
    private static final DataModel instance = new DataModel();
    private GameNotify gameNotify;

    private DataModel() {

    }

    public static DataModel getInstance() {
        return instance;
    }

    /**
     * 狼人數量
     */
    private int wolfCnt;
    /**
     * 總人數
     */
    private int peoCnt;
    /**
     * 腳色有無
     */
    private final LinkedHashMap<Action, Boolean> roleMap = new LinkedHashMap<>();
    /**
     * 遊戲規則
     */
    private boolean witchSaveRule; // True: 女巫第一晚可自救
    private boolean gameEndRule; // True: 屠城 False: 屠邊
    private boolean prettyWolfRule; // True: 狼美夜晚死亡可發動技能
    private boolean hunterKillRule; // True: 獵人有雙面刃

    /**
     * 遊戲中參數
     */
    private boolean isDay; //日夜
    private Action stage; //遊戲階段
    private int turn; //輪次
    private boolean gameEnd;
    private boolean hiddenWolfShowed;
    private final List<Action> stageOrder = new ArrayList<>(); //該局遊戲順序
    private final List<Integer> dieList = new ArrayList<>(); //死亡名單

    private final List<Action> actionList = new ArrayList<>(); //座位表對應職業
    private final HashMap<Action, Integer> godRoleMap = new HashMap<>(); //好人玩家對應的職業
    private final HashMap<Action, Integer> wolfRoleMap = new HashMap<>(); //狼人玩家對應的職業
    private final List<Integer> wolfGroup = new ArrayList<>(); //狼人名單
    private final List<Integer> villagers = new ArrayList<>(); //村民名單

    public void setGameNotify(GameNotify gameNotify) {
        this.gameNotify = gameNotify;
    }

    public void initGameVariable() {
        isDay = true;
        stage = Action.準備開始;
        turn = 1;
        gameEnd = false;
        hiddenWolfShowed = false;
        stageOrder.clear();
        dieList.clear();
        godRoleMap.clear();
        wolfRoleMap.clear();
        wolfGroup.clear();
        villagers.clear();
        actionList.clear();

        stageOrder.add(Action.準備開始);
        stageOrder.add(Action.狼人);
        stageOrder.add(Action.殺人);

        for (Action act : roleMap.keySet()) {
            if (roleMap.get(act)) {
                stageOrder.add(act);
            }
        }
        stageOrder.add(Action.白天);

        Log.d("DataModel ->", "stageOrder:" + stageOrder);
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public int getWolfCnt() {
        return wolfCnt;
    }

    public void setWolfCnt(int wolf_cnt) {
        this.wolfCnt = wolf_cnt;
    }

    public LinkedHashMap<Action, Boolean> getRoleMap() {
        return roleMap;
    }


    public boolean isWitchSaveRule() {
        return witchSaveRule;
    }

    public void setWitchSaveRule(boolean witchSaveRule) {
        this.witchSaveRule = witchSaveRule;
    }

    public void setGameEndRule(boolean gameEndRule) {
        this.gameEndRule = gameEndRule;
    }

    public void setPrettyWolfRule(boolean prettyWolfRule) {
        this.prettyWolfRule = prettyWolfRule;
    }

    public void setHunterKillRule(boolean hunterKillRule) {
        this.hunterKillRule = hunterKillRule;
    }

    public int getPeoCnt() {
        return peoCnt;
    }

    public void setPeoCnt(int peoCnt) {
        this.peoCnt = peoCnt;
    }


    public boolean isDay() {
        return isDay;
    }

    public void dayEnd() {
        isDay = false;
        gameNotify.notifyDayEnd();
    }

    public void nightEnd() {
        isDay = true;
        if (turn == 1) {
            addVillagers();
            initActionList();
        }
        gameNotify.notifyNightEnd();
    }

    public Action getStage() {
        return stage;
    }

    public void setNextStage() {
        stage = getNextStage();
        Log.d(TAG, "Next stage is " + stage);
        gameNotify.notifyStageChanged(stage);
    }

    public Action getNextStage() {
        for (int i = 0; i < stageOrder.size(); i++) {
            if (stageOrder.get(i) == stage) {
                if (i + 1 < stageOrder.size()) {
                    if (turn != 1 && stageOrder.get(i + 1).isPassive()) {
                        stage = stageOrder.get(i + 1);
                        continue;
                    }
                    return stageOrder.get(i + 1);
                } else {
                    return Action.狼人;
                }
            }
        }
        Log.e("error stage->", "unexpected error, do not have this stage." + stage);
        return Action.白天;
    }

    private void addVillagers() {
        for (int i = 1; i <= peoCnt; i++) {
            if (!(godRoleMap.containsValue(i) || wolfGroup.contains(i) || wolfRoleMap.containsValue(i))) {
                villagers.add(i);
            }
        }
    }


    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turn += 1;
    }

    public HashMap<Action, Integer> getGodRoleMap() {
        return godRoleMap;
    }

    public List<Integer> getDieList() {
        return dieList;
    }

    /**
     * 獲知玩家死亡，加入死亡名單
     *
     * @param seat 已確認死亡座位
     */
    public void playerDead(int seat) {
        dieList.add(seat);
        if (roleMap.get(Action.隱狼) && dieList.containsAll(wolfGroup) && !hiddenWolfShowed) {
            gameNotify.notifyShowHiddenWolf();
            hiddenWolfShowed = true;
        }
        checkGameEnd();
    }

    private void checkGameEnd() {
        if (gameEnd) {
            return;
        }
        int wolfDeadCnt = 0;
        for (int seat : dieList) {
            //算出死亡的狼的數量
            if (wolfGroup.contains(seat) || wolfRoleMap.containsValue(seat)) {
                wolfDeadCnt += 1;
            }
        }

        System.out.println(wolfDeadCnt);
        System.out.println(wolfGroup.size());
        System.out.println(wolfRoleMap.size());

        if (wolfDeadCnt == (wolfGroup.size() + wolfRoleMap.size())) {
            gameNotify.notifyGameEnd(EndType.好人勝利);
            gameEnd = true;
            return;
        }

        if (gameEndRule) {
            //屠城 除了狼 沒人活著
            if (godRoleMap.size() + villagers.size() + wolfDeadCnt == dieList.size()) {
                gameNotify.notifyGameEnd(EndType.屠城);
                gameEnd = true;
            }
        } else {
            //屠邊
            if (dieList.containsAll(villagers)) {
                gameNotify.notifyGameEnd(EndType.屠民);
                gameEnd = true;
            } else if (dieList.containsAll(godRoleMap.values())) {
                gameNotify.notifyGameEnd(EndType.屠神);
                gameEnd = true;
            }
        }
    }

    public List<Integer> getWolfGroup() {
        return wolfGroup;
    }

    public HashMap<Action, Integer> getWolfRoleMap() {
        return wolfRoleMap;
    }

    /**
     * 判斷這場遊戲有沒有這個階段
     *
     * @param stage 階段
     * @return 有無
     */
    public boolean hasStage(Action stage) {
        return stageOrder.contains(stage);
    }

    /**
     * 判斷是不是狼人死光了，刀子在地上
     */
    public boolean wolvesDead() {
        return dieList.containsAll(wolfGroup);
    }

    public boolean isFirstDay() {
        return turn == 1;
    }

    public void initActionList() {

        Log.d(TAG, "initActionList:" + villagers);

        actionList.add(null);
        for (int pos = 1; pos <= peoCnt; pos++) {
            Action posAction = null;
            if (godRoleMap.containsValue(pos)) {
                for (Action action : godRoleMap.keySet()) {
                    if (godRoleMap.get(action) == pos) {
                        posAction = action;
                        break;
                    }
                }
            }

            if (wolfRoleMap.containsValue(pos)) {
                for (Action action : wolfRoleMap.keySet()) {
                    if (wolfRoleMap.get(action) == pos) {
                        posAction = action;
                        break;
                    }
                }
            }

            if (wolfGroup.contains(pos)) {
                posAction = Action.狼人;
            }

            if (villagers.contains(pos)) {
                posAction = Action.村民;
            }

            actionList.add(posAction);
        }
    }
}
