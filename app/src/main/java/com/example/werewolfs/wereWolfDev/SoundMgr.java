package com.example.werewolfs.wereWolfDev;
/** 用途 : 語音*/

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoundMgr {
    public MediaPlayer mediaPlayer;
    private SoundPool sp;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private int order = 0;

    private List<MediaPlayer> cd = new ArrayList<>();

    public SoundMgr(Context context){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void playSound(Context context, int _id){
        mediaPlayer = MediaPlayer.create(context, _id);
        cd.add(mediaPlayer);
        order += 1;
        if(order != 1){
            if(cd.get(order-2).isPlaying()){
                cd.get(order-2).setOnCompletionListener(mp -> {
                    releaseSound(order - 2);
                    cd.get(order-1).start();
                });
            }else{
                cd.get(order-1).start();
            }
        }else{
            cd.get(order-1).start();
        }
    }

    public void cancelSound(){
        if(cd.get(order-1) != null){
            cd.get(order-1).pause();
        }
    }

    public void releaseSound(int order){
        if(cd.get(order) != null){
            cd.get(order).release();
        }
    }

//    public void preLoadSoundResource(Context context){
//        sp = new SoundPool(50, AudioManager.STREAM_SYSTEM, 5);
//        soundID.put(1 , sp.load(context, R.raw.bear_close, 1));
//        soundID.put(2 , sp.load(context, R.raw.bear_open, 1));
//        soundID.put(3 , sp.load(context, R.raw.bomb_skill, 1));
//    }
}