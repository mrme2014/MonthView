package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public class AmountTableHeadAdapter extends MyLinearLayoutBaseAdapter<String> {

    public AmountTableHeadAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    View getView(int position) {
        View convertView = getLayoutInflater().inflate(R.layout.item_amount_table_head, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(list.get(position));
        return convertView;
    }
}
