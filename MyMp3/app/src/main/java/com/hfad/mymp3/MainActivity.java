package com.hfad.mymp3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.mp3list);

        checkPermission();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void updateList(){
        if(!checkExStorage()){
            //사용 불가!!

        }else{
            // 사용이 가능하다면 File을 불러들여서 리스트를 출력
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

            ListItem[] items = makeList(path);

            MusicListItemAdapter adapter = new MusicListItemAdapter(this, items);

            listView.setAdapter(adapter);
        }
    }

    private void updateList(File path){
        if(!checkExStorage()){
            //사용 불가!!

        }else{
            // 사용이 가능하다면 File을 불러들여서 리스트를 출력
            ListItem[] items = makeList(path);
            MusicListItemAdapter adapter = new MusicListItemAdapter(this, items);

            listView.setAdapter(adapter);
        }
    }

    //경로를 받으면 ListItem 배열 객체를 만들어 냅니다.
    // 경로를 통해 아이템 문자열을 받고 폴더인지 음악 파일인지 그냥 일반 파일인지 판단하고
    private ListItem[] makeList(File path){
        File[] files = path.listFiles();        // 내부 파일들을 받습니다.
        ListItem[] items = new ListItem[files.length];

        for(int i = 0 ; i < files.length; i++){
            String itemName = files[i].getName();
            String itemPath = files[i].getPath();
            int type = 0;
            int resId = 0;
            if(files[i].isFile()){      // 만약 파일이면
                String exter = itemName.substring(itemName.indexOf('.') , itemName.length());
                if(exter.equals("mp3") || exter.equals("mp4") || exter.equals("wav") || exter.equals("wma") || exter.equals("aac") ||
                        exter.equals("flac")){
                    type = ListItem.MP3;
                    resId = R.drawable.music;
                }else {// 다른 파일일 경우
                    type = ListItem.OTHERS;
                    resId = R.drawable.files;
                }
            }else if(files[i].isDirectory()){       // 만약 폴더면
                type = ListItem.FOLDER;
                resId = R.drawable.folder;
            }
            items[i] = new ListItem(resId, itemName, type, itemPath);           // 각각의 리스트 아이템을 넣습니다.
        }
        return items;
    }

    // 외부 저장소가 있는가 없는가를 체크해서 있으면 사용가능 true를 없으면 false를 출력합니다.
    private boolean checkExStorage(){
        String state = Environment.getExternalStorageState();
        //사용가능하다면
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Log.d("test", "사용가능");
            return true;
        }else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("test", "읽기만 가능");
            return false;
        }else{
            Toast.makeText(getApplicationContext(),"사용 불가", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //읽기 퍼미션을 체크하는 메소드입니다.
    private void checkPermission(){
        int permisstionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permisstionCheck == PackageManager.PERMISSION_DENIED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, new String(Manifest.permission.READ_EXTERNAL_STORAGE))){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }


    //퍼미션 requestPermission의 결과 메소드입니다. 퍼미션 결과가 정상이라면 listView를 갱신합니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100:
                //퍼미션이 허락이 된다면
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateList();
                }else{// 만약 허락이 안된다면.
                    Toast.makeText(this, "퍼미션 설정을 해주셔야됩니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}


