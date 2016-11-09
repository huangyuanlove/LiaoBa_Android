package com.huangyuanlove.liaoba.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.adapter.MusicAdapter;
import com.huangyuanlove.liaoba.customui.SideBar;
import com.huangyuanlove.liaoba.entity.MusicBean;
import com.huangyuanlove.liaoba.service.PlayerService;
import com.huangyuanlove.liaoba.utils.ActivityCollector;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.PinYin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayMusicActivity extends Activity {


    private final int RECORD_AUDIO_CODE = 101;
    private final int REQUEST_OPEN_APPLICATION_SETTINGS_CODE = 12345;
    private Button startButton;
    private Intent playerIntent;
    private SeekBar seekBar;
    private TextView noMusic;
    private MyReceiver myReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private TextView timeTextView;
    private List<MusicBean> datas = new ArrayList<>();
    private PinYin pinYin = new PinYin();
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    // 获取扩展卡下的所有的音频文件接口
    private Uri mp3Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] columns = {MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,//文件名
            MediaStore.Audio.Media.DURATION,//音频时长
            MediaStore.Audio.Media.DATA//文件路径
    };

    private ListView listView;
    private MusicAdapter adapter;
    private SideBar sideBar;
    private TextView dialog;
    private Button EQButton;
    private MediaPlayer mMediaPlayer;
    private int playIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        initView();
        initData();
        event();

    }

    private void initData() {

        listView.setAdapter(adapter);
        ContentResolver musicResolve = getContentResolver();
        Cursor musicListCursor = musicResolve.query(mp3Uri, columns, null, null, null);
        while (musicListCursor.moveToNext()) {
            MusicBean bean = new MusicBean();
            String musicName = musicListCursor.getString(1);
            String sortkey = pinYin.String2Alpha(musicName).charAt(0) + "";
            int musicTime = 0;
            if (musicListCursor.getString(2) != null) {
                musicTime =
                        Integer.parseInt(musicListCursor.getString(2));
            }
            bean.setSortKey(sortkey);
            bean.setMusicName(musicName);
            bean.setMusicTime(sdf.format(musicTime));
            bean.setMusicPath(musicListCursor.getString(3));
            datas.add(bean);

        }

        Collections.sort(datas, new Comparator<MusicBean>() {
            @Override
            public int compare(MusicBean lhs, MusicBean rhs) {

                return lhs.getSortKey().compareTo(rhs.getSortKey());

            }
        });
        adapter = new MusicAdapter(datas, this);

        listView.setAdapter(adapter);


    }

    private void initView() {


        noMusic = (TextView) findViewById(R.id.no_music);
        EQButton = (Button) findViewById(R.id.EQ);
        EQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检测权限
                if (ContextCompat.checkSelfPermission(PlayMusicActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(PlayMusicActivity.this, EQActivity.class));
                }else{
                    ActivityCompat.requestPermissions(PlayMusicActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            RECORD_AUDIO_CODE);
                }

            }
        });
        listView = (ListView) findViewById(R.id.listView);
        dialog = (TextView) findViewById(R.id.music_dialog_text);
        sideBar = (SideBar) findViewById(R.id.sideBar);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                listView.setSelection(findIndexBySortKey(datas, s));
            }
        });
        timeTextView = (TextView) findViewById(R.id.time);
        startButton = (Button) findViewById(R.id.start);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playerIntent = new Intent(getApplicationContext(), PlayerService.class);
        myReceiver = new MyReceiver();
        localBroadcastManager = LocalBroadcastManager
                .getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(myReceiver, new IntentFilter(
                Config.ACTION_PROGRESS));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MusicBean musicBean = (MusicBean) parent.getItemAtPosition(position);
                String path = musicBean.getMusicPath();
                playIndex = position;
                playerIntent.putExtra(Config.EXSTRA_CHANGE, true);
                playerIntent.putExtra(Config.EXSTRA_PATH, path);
                startService(playerIntent);

            }
        });
        listView.setEmptyView(noMusic);
        // Create the MediaPlayer
        if (PlayerService.mPlayer == null) {
            PlayerService.mPlayer = MediaPlayer
                    .create(getApplicationContext(), R.raw.leidegaobai);
        }

        mMediaPlayer = PlayerService.mPlayer;
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (datas.size()==0)
                {
                    play(null);
                    return ;

                }
                playIndex += 1;
                if (playIndex > datas.size()) {
                    playIndex = 0;
                }
                playerIntent.putExtra(Config.EXSTRA_CHANGE, true);
                playerIntent.putExtra(Config.EXSTRA_PATH, datas.get(playIndex).getMusicPath());
                startService(playerIntent);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case RECORD_AUDIO_CODE:
                // 如果用户不允许，我们视情况发起二次请求或者引导用户到应用页面手动打开
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {

                    // 二次请求，表现为：以前请求过这个权限，但是用户拒接了
                    // 在二次请求的时候，会有一个“不再提示的”checkbox
                    // 因此这里需要给用户解释一下我们为什么需要这个权限，否则用户可能会永久不在激活这个申请
                    // 方便用户理解我们为什么需要这个权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PlayMusicActivity.this, permissions[0])) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(PlayMusicActivity.this).setTitle("权限申请").setMessage("们需要您允许我们调节音效，以方便您在播放音乐时感受不同的音效")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ActivityCompat.requestPermissions(PlayMusicActivity.this,
                                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                                        RECORD_AUDIO_CODE);
                                            }
                                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                    // 到这里就表示已经是第3+次请求，而且此时用户已经永久拒绝了，这个时候，我们引导用户到应用权限页面，让用户自己手动打开
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayMusicActivity.this).setTitle("权限申请")
                                .setMessage("请在打开的窗口的权限中开启音效调节权限，以正常使用本应用")
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        openApplicationSettings(REQUEST_OPEN_APPLICATION_SETTINGS_CODE);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.setCancelable(false);
                        builder.show();
                    }
                    return;
                }

                // 到这里就表示用户允许了本次请求
                startActivity(new Intent(PlayMusicActivity.this, EQActivity.class));
                break;

        }

    }

    private void event() {
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekProgress = seekBar.getProgress();

                Intent intent = new Intent(Config.ACTION_SEEKTO);
                intent.putExtra(Config.EXSTRA_PROGRESS_CUR, seekProgress);
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });

    }

    public void play(View v) {

        playerIntent.putExtra(Config.EXSTRA_CHANGE, false);

        startService(playerIntent);

        if(mMediaPlayer.isPlaying())
        {
            startButton.setText("播放");
        }
        else
        {
            startButton.setText("暂停");
        }

    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int maxLen = intent.getIntExtra(Config.EXSTRA_PROGRESS_MAX, 0);
            int currentLen = intent.getIntExtra(Config.EXSTRA_PROGRESS_CUR, 0);

            String totleTime = sdf.format(maxLen);
            String currentTime = sdf.format(currentLen);

            timeTextView.setText(currentTime + "/" + totleTime);
            seekBar.setMax(maxLen);
            seekBar.setProgress(currentLen);
        }

    }

    //根据sortKey找到索引位置
    private int findIndexBySortKey(List<MusicBean> list, String sortKey) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                MusicBean bean = list.get(i);
                if (sortKey.equals(bean.getSortKey())) {

                    return i;
                }
            }
        }
        return -1;
    }

    private boolean openApplicationSettings(int requestCode) {
        try {
            Intent intent =
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + PlayMusicActivity.this.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PlayMusicActivity.this.startActivityForResult(intent, requestCode);
            return true;
        } catch (Throwable e) {

        }
        return false;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OPEN_APPLICATION_SETTINGS_CODE:
                //检测权限
                if (ContextCompat.checkSelfPermission(PlayMusicActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(PlayMusicActivity.this, EQActivity.class));
                }else{
                    ActivityCompat.requestPermissions(PlayMusicActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            RECORD_AUDIO_CODE);
                }
                break;
        }
    }
}
