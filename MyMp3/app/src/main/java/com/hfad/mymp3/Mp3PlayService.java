package com.hfad.mymp3;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/*
이 클래스는 Mp3를 실행시키는 서비스입니다.
Mp3액티비티는 단순히 화면출력만 목적으로하는 코드로 목적을 삼습니다.
 */
public class Mp3PlayService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mp3Binder = new Mp3Binder();        //IBinder 인터페이스를 상속 구현한 mp3Binder객체입니다. 이걸로 액티비티와 그리고 알림창과 소통합니다.

    static MediaPlayer mp;                 // mediaPlayer 객체
    static int minute = 0;          //현재 분
    static int miliSecond = 0;      // 현재 초
    static int total_duration = 0;      // 현재 진행 시간
    private String name;            // 곡의 이름
    private String path;            // 곡의 경로

    // 바인더에는 Service를 리턴하는 메소드를 만듭니다.
    class Mp3Binder extends Binder {
        public Mp3PlayService getService() {
            return Mp3PlayService.this;
        }
    }

    // 여기에 Intent를 받고 경로와 이름을 받습니다.
    // 그리고 그것을 바탕으로 mp객체를 만듭니다.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        name = intent.getStringExtra("name");
        path = intent.getStringExtra("path");

        Uri.Builder builder = new Uri.Builder();
        builder.encodedPath(path);
        Uri uri = builder.build(); // 경로를 받고 이를 바탕으로 uri를 받습니다.

        mp = MediaPlayer.create(this, uri);

        return mp3Binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 어차피 서비스 바인드를 사용하니 상관은 없을 듯 하다.
    // 직접 서비스를 조작할 수 있으니까.!!
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 이 메소드는 MediaPlayer가 끝났을때 처리하는 메소드입니다.
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    //음악을 실행 시키는 메소드입니다.
    public void play() {
        mp.start();
    }
    //음악을 멈추는 메소드입니다.
    public void pause() {
        mp.pause();
    }

    public void mp3SeekTo(int msec){
        mp.seekTo(msec);
    }


    //곡의 mp , 경로와 이름의 세터와 게터입니다.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    static public MediaPlayer getMp() {
        return mp;
    }
}
