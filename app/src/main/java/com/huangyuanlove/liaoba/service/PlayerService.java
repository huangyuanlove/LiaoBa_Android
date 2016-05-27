package com.huangyuanlove.liaoba.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.utils.Config;

/*
 * 用于管理MediaPlayer的服务组件
 */

public class PlayerService extends Service {

    public static MediaPlayer mPlayer;
    private int sumLen;
    private LocalBroadcastManager localBroadcastManager;
    private SeekToReceiver myReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // 实例化MediaPalyer
        if(mPlayer == null) {
            mPlayer = MediaPlayer
                    .create(getApplicationContext(), R.raw.leidegaobai);
        }
            localBroadcastManager = LocalBroadcastManager
                .getInstance(getApplicationContext());
        myReceiver = new SeekToReceiver();
        localBroadcastManager.registerReceiver(myReceiver, new IntentFilter(Config.ACTION_SEEKTO));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断是否播放新文件
        if(intent.getBooleanExtra(Config.EXSTRA_CHANGE, false))
        {
            mPlayer.reset();
            try {
                //设置新的数据源    音频文件
                mPlayer.setDataSource(intent.getStringExtra(Config.EXSTRA_PATH));
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 判断当前的MediaPlayer是否正在播放
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
            new ProgressThread().start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
//        mPlayer.release();
        localBroadcastManager.unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    class ProgressThread extends Thread {
        @Override
        public void run() {
            try {
                while (mPlayer != null && mPlayer.isPlaying()) {
                    sumLen = mPlayer.getDuration();
                    int currentPosition = mPlayer.getCurrentPosition();
                    Intent intent = new Intent(Config.ACTION_PROGRESS);
                    intent.putExtra(Config.EXSTRA_PROGRESS_MAX, sumLen);
                    intent.putExtra(Config.EXSTRA_PROGRESS_CUR, currentPosition);

                    localBroadcastManager.sendBroadcast(intent);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SeekToReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int currentLen = intent.getIntExtra(Config.EXSTRA_PROGRESS_CUR, 0);
            mPlayer.seekTo(currentLen);
        }

    }
}