package hong.mymemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hong on 2018-03-07.
 */

public class MemoDB extends SQLiteOpenHelper {

    final static int DATABASE_VERSION = 1;

    public MemoDB(Context context) {
        super(context, "memodb", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String memoSQL = "create table tb_memo "+
                "(_id integer primary key autoincrement,"+
                "title," +
                "content," +
                "time)";

        db.execSQL(memoSQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_memo");
            onCreate(db);
        }
    }
}
