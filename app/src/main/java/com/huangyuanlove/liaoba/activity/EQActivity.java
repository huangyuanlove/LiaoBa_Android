package com.huangyuanlove.liaoba.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.service.PlayerService;

public class EQActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Normal;
    private TextView Classical;
    private TextView Dance;
    private TextView Flat;
    private TextView Folk;
    private TextView HeavyMetal;
    private TextView HipHop;
    private TextView Jazz;
    private TextView Pop;
    private TextView Rock;

    private TextView EQTextView[] = new TextView[10];

    private static final String TAG = "AudioFxActivity";

    private static final float VISUALIZER_HEIGHT_DIP = 160f;

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private Equalizer mEqualizer;

    private LinearLayout mLinearLayout;
    private VisualizerView mVisualizerView;
//    private TextView mInfoView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);


        mLinearLayout = new LinearLayout(this);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(mLinearLayout);

        // Create the MediaPlayer
        if(PlayerService.mPlayer == null)
        {
            PlayerService.mPlayer = MediaPlayer
                    .create(getApplicationContext(), R.raw.leidegaobai);
        }

        mMediaPlayer = PlayerService.mPlayer;

        setupVisualizerFxAndUI();
        setupEqualizerFxAndUI();

        mVisualizer.setEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();


         View view = getLayoutInflater().inflate(R.layout.include_eq_text,null);
        mLinearLayout.addView(view);

        Normal = (TextView) view.findViewById(R.id.Normal);
        EQTextView[0] = Normal;
        Normal.setOnClickListener(this);

        Classical = (TextView) view.findViewById(R.id.Classical);
        EQTextView[1] = Classical;
        Classical.setOnClickListener(this);

        Dance = (TextView) view.findViewById(R.id.Dance);
        EQTextView[2] = Dance;
        Dance.setOnClickListener(this);

        Flat = (TextView) view.findViewById(R.id.Flat);
        EQTextView[3] = Flat;
        Flat.setOnClickListener(this);

        Folk = (TextView) view.findViewById(R.id.Folk);
        EQTextView[4] = Folk;
        Folk.setOnClickListener(this);

        HeavyMetal = (TextView) view.findViewById(R.id.Heavy_Metal);
        EQTextView[5] = HeavyMetal;
        HeavyMetal.setOnClickListener(this);

        HipHop = (TextView) view.findViewById(R.id.Hip_Hop);
        EQTextView[6] = HipHop;
        HipHop.setOnClickListener(this);

        Jazz = (TextView) view.findViewById(R.id.Jazz);
        EQTextView[7] = Jazz;
        Jazz.setOnClickListener(this);

        Pop = (TextView) view.findViewById(R.id.Pop);
        EQTextView[8] = Pop;
        Pop.setOnClickListener(this);

        Rock = (TextView) view.findViewById(R.id.Rock);
        EQTextView[9] = Rock;
        Rock.setOnClickListener(this);

        short num = mEqualizer.getNumberOfPresets();

        for (short i = 0; i < num; i++) {

            System.out.println(i+":" + mEqualizer.getPresetName(i));

        }


    }

    private void setupEqualizerFxAndUI() {
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);



        TextView eqTextView = new TextView(this);
        eqTextView.setText("均衡器:");
        mLinearLayout.addView(eqTextView);

        short bands = mEqualizer.getNumberOfBands();

        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < bands; i++) {
            final short band = i;

            TextView freqTextView = new TextView(this);
            freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000)
                    + " Hz");
            mLinearLayout.addView(freqTextView);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(this);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEQLevel / 100) + " dB");

            TextView maxDbTextView = new TextView(this);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQLevel / 100) + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            SeekBar bar = new SeekBar(this);
            bar.setLayoutParams(layoutParams);
            bar.setMax(maxEQLevel - minEQLevel);
            bar.setProgress(mEqualizer.getBandLevel(band));

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    mEqualizer.setBandLevel(band, (short) (progress + minEQLevel));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            row.addView(minDbTextView);
            row.addView(bar);
            row.addView(maxDbTextView);

            mLinearLayout.addView(row);
        }
    }

    private void setupVisualizerFxAndUI() {
        mVisualizerView = new VisualizerView(this);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                (int) (VISUALIZER_HEIGHT_DIP * getResources()
                        .getDisplayMetrics().density)));
        mLinearLayout.addView(mVisualizerView);

        final int maxCR = Visualizer.getMaxCaptureRate();

        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(256);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] fft, int samplingRate) {
                        mVisualizerView.updateVisualizer(fft);
                    }
                }, maxCR / 2, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
        }
    }

    private void clickEQTextColor()
    {
        for (int i = 0; i < EQTextView.length; i++) {
            EQTextView[i].setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        clickEQTextColor();
        switch (v.getId()){
            case R.id.Normal:
                mEqualizer.usePreset((short)0);
                Normal.setTextColor(Color.CYAN);
                break;
            case R.id.Classical:
                mEqualizer.usePreset((short)1);
                Classical.setTextColor(Color.CYAN);
                break;
            case R.id.Dance:
                mEqualizer.usePreset((short)2);
                Dance.setTextColor(Color.CYAN);
                break;
            case R.id.Flat:
                mEqualizer.usePreset((short)3);
                Flat.setTextColor(Color.CYAN);
                break;
            case R.id.Folk:
                mEqualizer.usePreset((short)4);
                Folk.setTextColor(Color.CYAN);
                break;
            case R.id.Heavy_Metal:
                mEqualizer.usePreset((short)5);
                HeavyMetal.setTextColor(Color.CYAN);
                break;
            case R.id.Hip_Hop:
                mEqualizer.usePreset((short)6);
                HipHop.setTextColor(Color.CYAN);
                break;
            case R.id.Jazz:
                mEqualizer.usePreset((short)7);
                Jazz.setTextColor(Color.CYAN);
                break;
            case R.id.Pop:
                mEqualizer.usePreset((short)8);
                Pop.setTextColor(Color.CYAN);
                break;
            case R.id.Rock:
                mEqualizer.usePreset((short)9);
                Rock.setTextColor(Color.CYAN);
                break;
        }
    }

    class VisualizerView extends View {
        private byte[] mBytes;
        private float[] mPoints;
        private Rect mRect = new Rect();

        private Paint mForePaint = new Paint();
        private int mSpectrumNum = 48;
        private boolean mFirst = true;

        public VisualizerView(Context context) {
            super(context);
            init();
        }

        private void init() {
            mBytes = null;

            mForePaint.setStrokeWidth(8f);
            mForePaint.setAntiAlias(true);
            mForePaint.setColor(Color.rgb(0, 128, 255));
        }

        public void updateVisualizer(byte[] fft) {


            byte[] model = new byte[fft.length / 2 + 1];

            model[0] = (byte) Math.abs(fft[0]);
            for (int i = 2, j = 1; j < mSpectrumNum; ) {
                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
                i += 2;
                j++;
            }
            mBytes = model;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mBytes == null) {
                return;
            }

            if (mPoints == null || mPoints.length < mBytes.length * 4) {
                mPoints = new float[mBytes.length * 4];
            }

            mRect.set(0, 0, getWidth(), getHeight());


            //绘制频谱
            final int baseX = mRect.width() / mSpectrumNum;
            final int height = mRect.height();

            for (int i = 0; i < mSpectrumNum; i++) {
                if (mBytes[i] < 0) {
                    mBytes[i] = 127;
                }

                final int xi = baseX * i + baseX / 2;

                mPoints[i * 4] = xi;
                mPoints[i * 4 + 1] = height;

                mPoints[i * 4 + 2] = xi;
                mPoints[i * 4 + 3] = height - mBytes[i];
            }

            canvas.drawLines(mPoints, mForePaint);
        }
    }


}
