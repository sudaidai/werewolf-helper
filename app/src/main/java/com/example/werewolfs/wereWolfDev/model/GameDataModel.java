package com.example.werewolfs.wereWolfDev.model;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.werewolfs.wereWolfDev.constant.Action;
import com.example.werewolfs.wereWolfDev.constant.Static;
import com.example.werewolfs.wereWolfDev.viewModel.StageControlCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GameDataModel {

    StageControlCallback stageCallback;

    /**
     * variable
     */
    private int num_people;
    private boolean isDay;
    private int turn;
    private Context context;
    private Action stage;
    public List<Integer> wolfGroup = new ArrayList<>();
    private int totalWolf;

    /**
     * collection
     */
    public List<Integer> order = new ArrayList<>();
    public List<Integer> remove_order = new ArrayList<>();
    public List<Action> stageOrder = new ArrayList<>();
    public LinkedHashMap<Action, Boolean> hasJob = new LinkedHashMap<>();

    public GameDataModel(StageControlCallback stageCallback) {
        init();
        this.stageCallback = stageCallback;
    }

    private void init() {
        isDay = true;
        turn = 1;
        stage = Action.準備開始;
        for (int i = 0; i < Static.MAX_NUM_PEOPLE; i++) {
            order.add(i);
        }
    }

    public void setNum_People(int num) {
        num_people = num;
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = db.getReference("room");
//        myRef.setValue(num);
    }

    public int getNum_People() {
        return num_people;
    }

    public void removeBtnOrder(int num) {
        remove_order.add(num - 1);
        int minus = Static.MAX_NUM_PEOPLE - remove_order.size();
        if (minus == num_people) {
            order.removeAll(remove_order);
        }
    }

    public List<Integer> getOrder() {
        return order;
    }


    public boolean isDay() {
        return isDay;
    }

    public void setDay() {
        isDay = !isDay;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn() {
        turn += 1;
    }

    public void setAppContext(Context context) {
        this.context = context;
    }

    public Action getStage() {
        return stage;
    }

    public void setJob(LinkedHashMap<Action, Boolean> hasJob) {
        this.hasJob = hasJob;
        stageOrder.add(Action.準備開始);
        stageOrder.add(Action.狼人);
        stageOrder.add(Action.殺人);

        for (Action act : hasJob.keySet()) {
            if (hasJob.get(act) == true) {
                stageOrder.add(act);
            }
        }

        stageOrder.add(Action.白天);
        System.out.println("stageOrder:" + stageOrder);
    }

    public Action getNextStage() {
        for (int i = 0; i < stageOrder.size(); i++) {
            if (stageOrder.get(i) == stage) {
                if (i + 1 != stageOrder.size()) {
                    return stageOrder.get(i + 1);
                } else {
                    return Action.狼人;
                }
            }
        }
        System.err.println("unexpected error, do not have this stage." + stage);
        return Action.白天;
    }

    public void setStage() {
        stage = getNextStage();
        System.out.println("Next stage is " + stage);
        Log.d("改變狀態 : ", "123");

        boolean isFirstDay = (turn == 1);

        switch (stage){
            case 準備開始:
                break;
            case 狼人:
                stageCallback.callback("狼人請睜眼");
                break;
            case 殺人:
                break;
            case 隱狼:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 狼美人:
                break;
            case 狐仙:
                break;
            case 惡靈騎士:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 陰陽使者:
                break;
            case 女巫:
                break;
            case 石像鬼:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 魔術師:
                break;
            case 預言家:
                break;
            case 熊:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 獵人:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 騎士:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 白癡:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 炸彈人:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 禁言長老:
                break;
            case 守墓人:
                break;
            case 守衛:
                break;
            case 野孩子:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 混血兒:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 盜賊:
                if(!isFirstDay){
                    stage = getNextStage();
                }
                break;
            case 九尾妖狐:
                break;
            case 通靈師:
                break;
            case 石像鬼2:
                break;
            case 白天:
                new CountDownTimer(5000, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        stageCallback.callback(String.valueOf(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        stageCallback.callback("點此進入下一晚");
                    }
                };
                break;
        }
    }

    public int getTotalWolf() {
        return totalWolf;
    }

    public void setTotalWolf(int totalWolf) {
        this.totalWolf = totalWolf;
    }
}
