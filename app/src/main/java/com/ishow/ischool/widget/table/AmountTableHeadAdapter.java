package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ishow.ischool.R;

import java.util.List;

import static com.ishow.ischool.R.id.tv;

/**
 * Created by mini on 16/9/28.
 */

public class AmountTableHeadAdapter extends MyLinearLayoutBaseAdapter<String> {

    public AmountTableHeadAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    View getView(int position, View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.item_amount_table_head, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(tv);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
