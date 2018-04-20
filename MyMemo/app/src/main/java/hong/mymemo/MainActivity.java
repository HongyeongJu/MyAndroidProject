package hong.mymemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/*
작성 일 3월 7일
프로그램명 : 간단한 메모장
만들어야될 부분 : 3.어뎁터와 리스트 구현(커스텀 어뎁터로) . 1.데이터 베이스 구현, 4.리스트를 눌렀을 때 편집액티비티 만드는 것 구현. 2.편집 액티비티 구현.
 */
public class MainActivity extends AppCompatActivity {

    // fragment로 gabageCan을 구현한다. ==> 복원 기능.
    // 그리고 MainActivity의 리스트 내용 구현도 fragment로 구현한다.  => 설계할때부터 fragment를 신경쓰지 않고 만들었다. 따라서 그냥 액티비티로 구현하는게 나을듯;.

    // 뷰 이동해서 삭제 하는 것 구현 => ?
    // 그림 메모도 저장할 수 있게끔 그림 메모를 구현한다. => View Draw부분 공부

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpend;
    ListView listView;
    FloatingActionButton fb;
    ArrayList<DriveVO> datas;

    //초기화 및 리스트 뷰 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);        // 타이틀 없에기

        drawer = (DrawerLayout)findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id=item.getItemId();
                if(id == R.id.menu_drawer_name){
                    makeDialog("만든이 : 홍영주 \n 이메일 : abc123472000@naver.com" , 0);

                }else if(id == R.id.menu_drawer_version){
                    makeDialog("버전 정보 : 1.0", 1);
                }
                return false;
            }
        });

        //테스트용
        //testMemo();
// 리스트뷰를 만든다.
        listView = (ListView)findViewById(R.id.listview);

        // 데이터 베이스 처리용
        MemoDB helper = new MemoDB(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_memo", null);
        datas = new ArrayList<>();

        while(cursor.moveToNext()){
            DriveVO vo = new DriveVO();
            vo.title = cursor.getString(1);
            vo.content = cursor.getString(2);
            vo.date = cursor.getString(3);
            vo.id = cursor.getString(0);
            datas.add(vo);
        }
        db.close();

// 리스트 뷰에 관한 내용
        DriveAdapter driveAdapter = new DriveAdapter(this, R.layout.item_view, datas);
        listView.setAdapter(driveAdapter);
        // 리스트뷰를 눌렀을 때 새로운 편집 액티비티를 띄운다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DriveAdapter adapter = (DriveAdapter)listView.getAdapter();
                DriveVO vo = adapter.getItem(position);

                String presentId = vo.id;
                Log.d("vo.id는 지금: " , String.valueOf(vo.id));

                final int ITIS =1;      // 있는 것을 수정한다면

                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("id", presentId);
                intent.putExtra("condition", ITIS);
                startActivityForResult(intent, 20);     // 기존에 있는 것은 20
            }
        });

        // 플로팅 버튼을 눌렀을 때 관한 처리 (새로 만들때)
        fb = (FloatingActionButton)findViewById(R.id.main_floatingbutton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int NEW =0;
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                intent.putExtra("condition", NEW);
                startActivityForResult(intent, 10);         // 새로이 만든다면 10
            }
        });
    }

// 다이얼 로그를 만들어주는 메소드
    private void makeDialog(String str, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            builder.setIcon(android.R.drawable.ic_menu_myplaces);
            builder.setTitle("개발자");
        }
        else if(id ==1){
            builder.setIcon(android.R.drawable.ic_menu_help);
            builder.setTitle("버전 정보");
        }
        builder.setMessage(str);
        builder.setPositiveButton("확인", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // 옆에 이동하는 ViewPageing을 메뉴 버튼으로 눌렀을때 인지 할 수 있도록 하는 것
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    //이건 그냥 테스트 용 메소드이다. ( 그냥 db 만들어서 테스트를 위한 메소드)
    private void testMemo(){
        MemoDB helper = new MemoDB(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from tb_memo");
        db.execSQL("insert into tb_memo (title, content , time) values (?,?,?)", new String[]{"안녕","하세요.","10년2월"});
        db.execSQL("insert into tb_memo (title, content, time) values (?,?,?)", new String[]{"안녕","하세요.","10년2월"});
        db.execSQL("insert into tb_memo (title, content, time) values (?,?,?)", new String[]{"안녕","하세요.","10년2월"});
        db.close();
    }

    //내가 보낸 액티비티에서 결과를 받음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateListView(); // 화면 갱신하기
        super.onActivityResult(requestCode, resultCode, data);
    }

    //리스트 뷰를 새로 갱신하는 메소드
    public void updateListView(){
        // 상황 갱신 짜보기
        datas.clear();      // 먼저 데이터(어레이 리스트를 없에고)
        MemoDB helper = new MemoDB(this);       // 데이터베이스를 연다음
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_memo",null);
        while(cursor.moveToNext()){
            DriveVO vo =new DriveVO();
            vo.title = cursor.getString(1);
            vo.content = cursor.getString(2);
            vo.date = cursor.getString(3);
            vo.id = cursor.getString(0);
            datas.add(vo);      // 새로이 리스트에 넣을 데이터를 다시 넣는다.
        }
        DriveAdapter temp = new DriveAdapter(this, R.layout.item_view, datas);
        listView.setAdapter(temp);      // 새로운 어뎁터로 보여준다.
    }
}