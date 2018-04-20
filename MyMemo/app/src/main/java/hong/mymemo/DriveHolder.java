package hong.mymemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Hong on 2018-03-07.
 */

public class DriveHolder {
    public TextView titleView;
    public TextView dateView;
    public TextView contentView;
    public ImageView deleteView;

    public DriveHolder(View root){
        titleView = (TextView)root.findViewById(R.id.item_title);
        dateView = (TextView)root.findViewById(R.id.item_date);
        contentView = (TextView)root.findViewById(R.id.item_content);
        deleteView = (ImageView)root.findViewById(R.id.item_delete);
    }
}
