package com.wf.werewolfs.wereWolfDev.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wf.werewolfs.R;
import com.wf.werewolfs.databinding.ActivityGameBinding;
import com.wf.werewolfs.wereWolfDev.constant.Action;
import com.wf.werewolfs.wereWolfDev.model.DataModel;
import com.wf.werewolfs.wereWolfDev.model.Role;
import com.wf.werewolfs.wereWolfDev.model.job.Seer;
import com.wf.werewolfs.wereWolfDev.model.job.TombKeeper;
import com.wf.werewolfs.wereWolfDev.viewModel.GameViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameActivity extends AppCompatActivity implements GameActivityNotify {

    /**
     * main class
     */
    private GameViewModel gameViewModel;

    /**
     * view
     */
    private ToggleButton[] tgBtn = new ToggleButton[19];

    /**
     * auto-generating class
     */
    public ActivityGameBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** main class init*/
        gameViewModel = new GameViewModel(getApplication());
        gameViewModel.initGameVariable();
        gameViewModel.setGameActivityNotify(this);

        /** data_binding*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setViewModel(gameViewModel);
        binding.setCBField(gameViewModel.ctrlBtnField);

        LinearLayout top = binding.top;
        LinearLayout bottom = binding.bottom;

        /** create toggleBtn by count of people*/
        int peoCnt = DataModel.getInstance().getPeoCnt();
        for (int i = 1; i <= peoCnt / 2; i++) {
            top.addView(createToggleBtn(i));
        }
        //希望座位是順時鐘，下面的座位反序放入
        for (int i = peoCnt; i > peoCnt / 2; i--) {
            bottom.addView(createToggleBtn(i));
        }

        stageInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameViewModel.createSound();
    }

    private void stageInit() {
        gameViewModel.setTgBtn(tgBtn);
    }

    private ToggleButton createToggleBtn(int pos) {
        tgBtn[pos] = new ToggleButton(this);
        tgBtn[pos].setId(pos);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        tgBtn[pos].setLayoutParams(params);
        tgBtnInit(tgBtn[pos]);
        return tgBtn[pos];
    }

    public void tgBtnInit(ToggleButton tgBtn) {
        tgBtn.setText(String.valueOf(tgBtn.getId()));
        tgBtn.setTextOn(tgBtn.getId() + "");
        tgBtn.setTextOff(tgBtn.getId() + "");
        tgBtn.setBackgroundColor(Color.WHITE);
        tgBtn.setTextColor(Color.BLACK);
        tgBtn.setClickable(false);
        tgBtn.setTextSize(40.0f);
    }


    @Override
    public void notifyRepeatSelect() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("身分重複")
                .setMessage("遊戲自動重新開始，別亂按好嗎 (☞ﾟ∀ﾟ)ﾟ∀ﾟ)☞")
                .setPositiveButton("ok", (dialog, which) -> {
                    gameViewModel.initGameVariable();
                    finish();
                })
                .show();
    }

    @Override
    public void notifySeerAsk(boolean isWolf, int seat, Seer seer) {
        String seerMessage = "";
        ImageView iv = new ImageView(this);
        if (isWolf) {
            seerMessage = seat + "號玩家是位狼人OvO";
            iv.setImageResource(R.drawable.werewolf);

        } else {
            seerMessage = seat + "號玩家是位正直的好人●v●";
            iv.setImageResource(R.drawable.not_werewolf);
        }

        AlertDialog seerDialog = new AlertDialog.Builder(GameActivity.this)
                .setCancelable(false).setView(iv)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("查驗結果為:" + seerMessage)
                .setPositiveButton("ok", null)
                .create();
        seerDialog.show();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                seerDialog.cancel();
                gameViewModel.closeYourEyes(seer);
            }
        }.start();
    }

    @Override
    public void notifyDaybreak(String message) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle(message)
                .setPositiveButton("ok", null)
                .show();
    }

    @Override
    public void notifyFirstDaybreak(String message, List<Integer> dieList) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle(message)
                .setPositiveButton("ok", (dialog, which) -> {
                    String message2 = gameViewModel.checkDieList(dieList);
                    new AlertDialog.Builder(this).setCancelable(false)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setTitle(message2)
                            .setPositiveButton("ok", null)
                            .show();
                    gameViewModel.initSeatState();
                })
                .show();
    }

    @Override
    public void notifyVoteCheck(int seat) {
        new AlertDialog.Builder(GameActivity.this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("在白天殺死 :" + seat + " 號玩家?")
                .setPositiveButton("投票", (dialog, which) -> {
                    gameViewModel.voteOn(seat);
                }).setNegativeButton("QQ", (dialog, which) -> {
        }).create().show();
    }

    @Override
    public void notifyGameEnd(String endTitle, String endMessage) {
        new AlertDialog.Builder(GameActivity.this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle(endTitle)
                .setMessage(endMessage)
                .setPositiveButton("ok", (dialog, which) -> {
                    finish();
                }).show();
    }

    @Override
    public void notifyWolfFriend(List<Integer> wolfGroup) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.werewolf)
                .setTitle(wolfGroup + " 是你的狼隊友")
                .setPositiveButton("ok", null)
                .show();
    }

    @Override
    public void notifyPrettyWolfDead(int lover) {
        new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.dead_with_pretty)
                .setTitle(lover + " 玩家殉情了♥...")
                .setPositiveButton("ok", null)
                .show();
    }

    @Override
    public void notifySeeThrough(Action identity, int seat, Role role) {
        AlertDialog seeThroughDialogue = new AlertDialog.Builder(GameActivity.this)
                .setCancelable(false)
                .setIcon(identity.role.imageId)
                .setTitle(seat + "號玩家為:" + identity)
                .setPositiveButton("ok", null)
                .create();
        seeThroughDialogue.show();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                seeThroughDialogue.cancel();
                if (role.stage == Action.石像鬼) {
                    gameViewModel.gargoyleGetKnife();
                } else {
                    gameViewModel.closeYourEyes(role);
                }
            }
        }.start();
    }

    @Override
    public void notifyInGrave(boolean identity, TombKeeper tombKeeper) {
        String isWolf = identity ? "好人" : "壞人";
        AlertDialog tombDialogue = new AlertDialog.Builder(this).setCancelable(false)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("前一晚你埋了一個... " + isWolf)
                .setPositiveButton("ok", null)
                .show();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                tombDialogue.cancel();
                gameViewModel.closeYourEyes(tombKeeper);
                gameViewModel.setAllSeatState(true);
            }
        }.start();
    }
}
