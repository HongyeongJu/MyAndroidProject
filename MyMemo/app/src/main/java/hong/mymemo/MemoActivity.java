package hong.mymemo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText memoTitle;
    EditText memoContent;
    ImageView memoCancel;
    ImageView memoDelete;
    ImageView memoOk;

    int condition;
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoTitle = (EditText)findViewById(R.id.memo_title);
        memoContent = (EditText)findViewById(R.id.memo_content);
        memoCancel = (ImageView)findViewById(R.id.memo_cancel);
        memoDelete = (ImageView)findViewById(R.id.memo_delete);
        memoOk = (ImageView)findViewById(R.id.memo_ok);
        // 인텐트를 받아서 어떤 데이터를 받았는지 확인해서 처리해야됨.
        // 신규인 경우 , 원래 있는 경우 (id로 판별한다).

        itemId = getIntent().getStringExtra("id");
        condition = getIntent().getIntExtra("condition",0);

        if(condition == 1){           // 새롭게 화면을 초기화 하는 메소드
            Log.d("지금memo에서의 item id값은: ", itemId);
            MemoDB helper = new MemoDB(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from tb_memo where _id = ?",new String[]{itemId});     //id는 1부터 시작한다.
            while(cursor.moveToNext()) {
                memoTitle.setText(cursor.getString(1));
                memoContent.setText(cursor.getString(2));
            }
        }
        else if(condition == 0){
        }

        memoCancel.setOnClickListener(this);
        memoDelete.setOnClickListener(this);
        memoOk.setOnClickListener(this);

    }

    //뷰를 클릭했을 때
    @Override
    public void onClick(View v) {
        //저장하는 것을 구현해야됨. 구별을 해야된다. 일단 들어갔는게 그게 새로 만들어서 들어갔는지. 아님 기존에 있던것을 가지고 들어갔는지를 비교해야됨.
        if(v == memoOk){
            String title = memoTitle.getText().toString();
            String content = memoContent.getText().toString();

            MemoDB memoDb = new MemoDB(this);
            SQLiteDatabase db = memoDb.getWritableDatabase();
            String time = nowDate();
            if(condition == 0)// 만약 새롭게 만든거라면 => 새롭게 저장을 해야될 것이고
            {
                db.execSQL("insert into tb_memo (title, content, time) values (?,?,?)", new String[]{title, content, time});
                makeToast("저장되었습니다.");
            }else if(condition == 1){   //  만약 새롭게 만든 것이 아니라 기존의 것을 이용했더라면. => 수정을 해야된다.
                //근데 어떤것인지 어케 알지? id를 받아야되는데..
                db.execSQL("update tb_memo set title = ?, content = ?, time = ?where _id = ?", new String[]{title, content, time, String.valueOf(itemId)});
                makeToast("저장되었습니다.");
            }
            db.close();
            finish();

            //Log.d("저장했을때 id값은 ? " , itemId);
        }else if(v == memoCancel){
            // 완료 (무시해도됨)
            // 그냥 액티비티를 닫는다. (일단 다이얼 로그로 묻는다 그냥 닫을 건지 말이다).
            // 완료
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("닫기");
            builder.setMessage("저장없이 그냥 닫으시겠습니까?");
            builder.setNegativeButton("아니요.", null);
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_info);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else if(v == memoDelete){
            // 일단 수정하고 수정한 것을 데이터베이스에서 지우면 되겠다.

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("삭제");
            builder.setMessage("정말로 삭제하겠습니까?");
            builder.setNegativeButton("아니요", null);
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 수정하고 지운다.  update 문
                    MemoDB memoDb = new MemoDB(getApplicationContext());
                    SQLiteDatabase db = memoDb.getWritableDatabase();
                    Log.d("삭제할 때 지금 id 값은?", itemId);
                    db.execSQL("delete from tb_memo where _id = ?" , new String[]{String.valueOf(itemId)});
                    db.close();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // 단순히 토스트 메시지를 출력하는 메소드
    private void makeToast(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
    // 현재 시간을 출력하는 메소드
    private String nowDate(){
        long now =System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        String nowTime = sdf.format(date);
        return nowTime;
    }
}
