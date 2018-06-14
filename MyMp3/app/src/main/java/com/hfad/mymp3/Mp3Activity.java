package com.hfad.mymp3;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class Mp3Activity extends AppCompatActivity {

    ImageView play_stop_button;         // 플레이 스톱 버튼
    boolean isPlay = false;             // 현재 실행중인지 확인하는 변수
    Handler handler;                    // 실행될때의 핸들러객체
    TextView progress_tv;                  // 진행중인 숫자 텍스트
    TextView end_time_tv;                  // 끝나는 시간 숫자 텍스트 객체
    SeekBar seekBar;                    // 시크바 객체

    static int miliSecond =0;                   // 현재 초
    static Mp3PlayService mp3Service;              // 실제로 음악을 재생시키는 서비스입니다.
    //서비스 커넥션 객체를 정의 합니다 .
    // 서비스 커넥션 객체는 서비스가 연결되었을때 , 강제로 종료되었을 때의 처리를 할수 있습니다.
    static ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Mp3PlayService.Mp3Binder mb = (Mp3PlayService.Mp3Binder)service;
            mp3Service = mb.getService();           //이제부터는 무조건 Service에서 음악이 실행되게끔 합니다.
        }

        //서비스가 강제로 종료되었을 때 호출되는 메소드입니다.
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // 이 액티비티가 실행되자마자. 음악이 실행되어야된다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);

        //Main으로부터 받은 Intent를 받고 이름과 경로를 받아옵니다.
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String path = intent.getStringExtra("path");
        Log.d("path", path);


        Intent serviceIntent = new Intent(Mp3Activity.this , Mp3PlayService.class);
        serviceIntent.putExtra("name", name);
        serviceIntent.putExtra("path", path);
        bindService(serviceIntent, Mp3Activity.connection, Service.BIND_AUTO_CREATE);


        play_stop_button = (ImageView)findViewById(R.id.play);
        progress_tv = (TextView)findViewById(R.id.progress);
        end_time_tv = (TextView)findViewById(R.id.end);
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        play_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 음악이 재생중이라면 pause를 실행시키고 play 버튼 모양으로 바꿉니다.
                if(Mp3PlayService.getMp().isPlaying()){
                    pause();
                }else{ // 음악이 일시정지중이라면 play를 실행시키고 pause 모양으로 바꿉니다.
                    play();
                }
            }
        });

        // 시크바를 움직여서 음악제어를 합니다.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 프로그레스가 진행이되면 ProgressTextView만 갱신되게끔 만들기
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int position = progress * Mp3PlayService.getMp().getDuration() / 100;           // 현재 위치를 비율로 알아냅니다. position은 현재 위치입니다.
                String str = String.format(Locale.getDefault(), "%d:%02d", position / 60000, (position / 1000) % 60);
                progress_tv.setText(str);
            }
            // 시크바를 트레킹을 할때 음악의 경과를 바꿉니다.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarTracking(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarTracking(seekBar);
            }
        });

    }

    // SeekBar를 움직일 때 음악이랑 progress 텍스트뷰에 현재 시간을 알려줌으로 인터페이스를 처리하는 메서드입니다.
    private void seekBarTracking(final SeekBar seekBar){
        // Thread Queue에 맨 앞에 보내서 다른 처리를 무시하고 처리할 수 있도록 합니다.
        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                int sek_progress = seekBar.getProgress();
                mp3Service.mp3SeekTo(sek_progress * Mp3PlayService.getMp().getDuration() / 100);       // 음악진행을 바꾸기
                updateProgressTextView();
            }
        });
    }

    // 음악을 실행 시키는 메소드입니다.
    private void play(){
        //서비스 에서 직접 플레이 시킵니다.
        mp3Service.play();
        play_stop_button.setImageResource(R.drawable.pause);
        // 음악 플레이어가 몇분짜리인지 표시해줍니다.
        int m = Mp3PlayService.getMp().getDuration() / 60000;
        int s = (Mp3PlayService.getMp().getDuration() / 1000) % 60;
        String str1 = String.format(Locale.getDefault(), "%d:%02d", m, s);
        end_time_tv.setText(str1);

        // 시크바 진행 수준을 표현해주기위해 핸들러 처리를 합니다.
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(Mp3PlayService.getMp().isPlaying()){     // 플레이중이라면 시간 표현을 증가시켜준다.
                    updateProgressTextView();
                    seekBar.setProgress((int)(((double)miliSecond / Mp3PlayService.getMp().getDuration()) * 100));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    // 이 메서드는 현재 진행되고 있는 시간을 갱신하는 메서드입니다.
    private void updateProgressTextView(){
        miliSecond = Mp3PlayService.getMp().getCurrentPosition();       //현재 음악의 milisecond 단위로 위치를 받습니다. 이 변수를 통해서 seekbar의 위치와 진행 경과 텍스트를 변화시킵니다.
        Log.d("milisecond", String.valueOf(miliSecond));
        String str = String.format(Locale.getDefault(), "%d:%02d", miliSecond / 60000 , (miliSecond / 1000) % 60);
        progress_tv.setText(str);
    }

    // 이 메소드는 음악을 일시 정지하는 메소드입니다.
    private void pause() {
        mp3Service.pause();
        play_stop_button.setImageResource(R.drawable.play);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
