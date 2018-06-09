package com.hfad.mymp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class Mp3Activity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    MediaPlayer mp;                     // mediaPlayer 객체
    ImageView play_stop_button;         // 플레이 스톱 버튼
    boolean isPlay = false;             // 현재 실행중인지 확인하는 변수
    Handler handler;                    // 실행될때의 핸들러객체
    TextView progress_tv;                  // 진행중인 숫자 텍스트
    TextView end_time_tv;                  // 끝나는 시간 숫자 텍스트 객체
    SeekBar seekBar;                    // 시크바 객체

    static int minute =0;                   // 현재 분
    static int miliSecond =0;                   // 현재 초
    static int total_duration = 0;          // 현재 진행 시간

    // 이 액티비티가 실행되자마자. 음악이 실행되어야된다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);

        //객체들을 다 할당 시킨다.
        mp = MediaPlayer.create(this, R.raw.baram);     // 임시적으로 바람의나라 음악을 튼다.
        play_stop_button = (ImageView)findViewById(R.id.play);
        progress_tv = (TextView)findViewById(R.id.progress);
        end_time_tv = (TextView)findViewById(R.id.end);
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        play_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 음악이 재생중이라면 pause를 실행시키고 play 버튼 모양으로 바꿉니다.
                if(mp.isPlaying()){
                    pause();
                    play_stop_button.setImageResource(R.drawable.play);
                }else{ // 음악이 일시정지중이라면 play를 실행시키고 pause 모양으로 바꿉니다.
                    play();
                    play_stop_button.setImageResource(R.drawable.pause);
                }
            }
        });

        // 시크바를 움직여서 음악제어를 합니다.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            // 시크바를 트레킹을 할때 음악의 경과를 바꿉니다.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int sek_progress = seekBar.getProgress();
                mp.seekTo(sek_progress * mp.getDuration() / 100);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // 음악을 실행 시키는 메소드입니다.
    private void play(){
        mp.start();

        // 음악 플레이어가 몇분짜리인지 표시해줍니다.
        int m = mp.getDuration() / 60000;
        int s = (mp.getDuration() / 1000) % 60;
        String str1 = String.format(Locale.getDefault(), "%d:%02d", m, s);
        end_time_tv.setText(str1);

        // 시크바 진행 수준을 표현해주기위해 핸들러 처리를 합니다.
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(mp.isPlaying()){     // 플레이중이라면 시간 표현을 증가시켜준다.
                    miliSecond = mp.getCurrentPosition();       //현재 음악의 milisecond 단위로 위치를 받습니다. 이 변수를 통해서 seekbar의 위치와 진행 경과 텍스트를 변화시킵니다.
                    Log.d("milisecond", String.valueOf(miliSecond));
                    String str = String.format(Locale.getDefault(), "%d:%02d", miliSecond / 60000 , (miliSecond / 1000) % 60);
                    seekBar.setProgress((int)(((double)miliSecond / mp.getDuration()) * 100));
                    progress_tv.setText(str);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    // 이 메소드는 음악을 일시 정지하는 메소드입니다.
    private void pause() {
        mp.pause();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
