package hong.mymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hong on 2018-03-07.
 */

public class DriveAdapter extends ArrayAdapter<DriveVO> {

    Context context;
    int resId;
    ArrayList<DriveVO> datas;
    int presentPosition;            // 단순히 position을 담기위한 값.

    // 내가 수정해야될듯하다 3월 08일
    @Nullable
    @Override
    public DriveVO getItem(int position) {
        return datas.get(position);
    }

    public DriveAdapter(Context context, int resId, ArrayList<DriveVO> datas){
        super(context,resId);
        this.context= context;
        this.resId = resId;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final DriveVO vo = datas.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);        //resId는 여기서 레이아웃 주소값
            DriveHolder holder=new DriveHolder(convertView);
            convertView.setTag(holder);
        }
        DriveHolder holder = (DriveHolder)convertView.getTag();     // 주소값 저장시키기


        TextView titleView = holder.titleView;
        TextView contentView = holder.contentView;
        TextView dateView = holder.dateView;
        ImageView deleteView = holder.deleteView;


        titleView.setText(vo.title);
        contentView.setText(vo.content);
        dateView.setText(vo.date);

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           // 리스트 아이템안에서 삭제 버튼을 눌렀을 시
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("삭제");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //삭제 버튼이 눌려졌을 때.
                        // id를 추출. 그리고 그 id로 삭제를 시도한다.
                        ///  => 여기를 만들어야됨. (삭제 구현)
                        MemoDB helper = new MemoDB(getContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("delete from tb_memo where _id=?", new String[]{vo.id});
                        db.close();
                        //삭제하고 바로 갱신하는 코드 => db를 다시 업데이트를 해야된다.
                        MainActivity main = (MainActivity)((ListView)parent).getContext(); // 먼저 MainActivity의 객체 main을 만들고 getContext로 mainActivity의 객체를 받는다.
                       // main.updateListView();  // 그리고 내가 미리 만들어놓은 메소드를 이용해서 새롭게 갱신하면 된다.
                    }
                });
                builder.setNegativeButton("아니오", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }
}
