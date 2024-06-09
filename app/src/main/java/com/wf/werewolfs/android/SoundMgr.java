package com.wf.werewolfs.android;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.PowerManager;
import android.util.Log;

import com.wf.werewolfs.R;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class SoundMgr implements MediaPlayer.OnCompletionListener {

    private static volatile SoundMgr instance;
    private static final String TAG = "Sound";
    private final Context mContext;
    public MediaPlayer mediaPlayer; // Currently playing
    public MediaPlayer cd;
    private SoundPool sp;
    private HashMap<Integer, Integer> soundID = new HashMap<>();
    private ArrayBlockingQueue<MediaPlayer> queue = new ArrayBlockingQueue<>(20);

    public static SoundMgr getInstance(Context context) {
        if (instance == null) {
            synchronized (SoundMgr.class) {
                if (instance == null) {
                    instance = new SoundMgr(context);
                }
            }
        }
        return instance;
    }

    private SoundMgr(Context context) {
        mContext = context.getApplicationContext();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void playSound(int _id) {
        Log.d(TAG, "play -> " + _id);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            cd = MediaPlayer.create(mContext, _id);
            cd.setOnCompletionListener(this);
            queue.offer(cd);
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(mContext, _id);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    public void cancelSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void preLoadSoundResource(Context context) {
        sp = new SoundPool(50, AudioManager.STREAM_MUSIC, 5);
        soundID.put(1, sp.load(context, R.raw.bear_close, 1));
        soundID.put(2, sp.load(context, R.raw.bear_open, 1));
        soundID.put(3, sp.load(context, R.raw.bomb_skill, 1));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (queue.peek() != null) {
            mediaPlayer.release();
            Log.d(TAG, "onCompletion: playing next sound.");
            mediaPlayer = queue.poll();
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (sp != null) {
            sp.release();
            sp = null;
        }
        queue.clear();
    }
}
