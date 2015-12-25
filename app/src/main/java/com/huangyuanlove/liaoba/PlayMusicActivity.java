package com.huangyuanlove.liaoba;

import java.text.SimpleDateFormat;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.huangyuanlove.liaoba.service.PlayerService;
import com.huangyuanlove.liaoba.utils.Config;

public class PlayMusicActivity extends AppCompatActivity {

    Intent playerIntent;
    Button stopButton;
    Button startButton;
    boolean isPlayer;
    SeekBar seekBar;
    MyReceiver myReceiver;
    LocalBroadcastManager localBroadcastManager;
    TextView timeTextView;

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    // 获取扩展卡下的所有的音频文件接口
    private Uri mp3Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] columns = { MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,//文件名
            MediaStore.Audio.Media.DATA, //文件路径
            MediaStore.Audio.Media.DURATION//音频时长
    };


    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        timeTextView = (TextView) findViewById(R.id.time);
        startButton = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playerIntent = new Intent(getApplicationContext(), PlayerService.class);
        myReceiver = new MyReceiver();
        localBroadcastManager = LocalBroadcastManager
                .getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(myReceiver, new IntentFilter(
                Config.ACTION_PROGRESS));

        event();

        initMp3List();
        getLoaderManager().initLoader(1, null, new LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), mp3Uri, columns, null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // TODO Auto-generated method stub

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(position);
                String name = cursor.getString(1);
                String path = cursor.getString(2);

                playerIntent.putExtra(Config.EXSTRA_CHANGE, true);
                playerIntent.putExtra(Config.EXSTRA_PATH, path);


                startService(playerIntent);

            }
        });
    }

    private void initMp3List() {
        listView = (ListView) findViewById(R.id.listView);

        adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.item_audio,
                null,
//                new String[]{columns[1],columns[2],columns[3]},
                new String[]{columns[1],columns[3]},
                new int[]{R.id.nameId,R.id.timeId},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER){
            @Override
            public void setViewText(TextView v, String text) {
                super.setViewText(v, text);

                if(v.getId() == R.id.timeId)
                {
                    if(text == "" || text==null)
                        v.setText("");
                    else
                    {
                        long time = Long.parseLong(text);
                        v.setText(sdf.format(time));
                    }
                }
            }
        };

        listView.setAdapter(adapter);

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
        isPlayer = !isPlayer;
        if (isPlayer) {
            startButton.setText("暂停");
        } else {
            startButton.setText("播放");
        }

    }

    public void stop(View v) {
        stopService(playerIntent);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int maxLen = intent.getIntExtra(Config.EXSTRA_PROGRESS_MAX, 0);
            int currentLen = intent.getIntExtra(Config.EXSTRA_PROGRESS_CUR, 0);

            String totleTime = sdf.format(maxLen);
            String currentTime = sdf.format(currentLen);

            timeTextView.setText(currentTime + "/" +  totleTime);

            seekBar.setMax(maxLen);
            seekBar.setProgress(currentLen);
        }

    }

}
