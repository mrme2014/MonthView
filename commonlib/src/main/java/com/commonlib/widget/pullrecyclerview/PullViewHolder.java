package com.commonlib.widget.pullrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by abel on 16/10/10.
 */

public class PullViewHolder extends RecyclerView.ViewHolder {

    private AdapterView.OnItemClickListener onItemClickListener;

    public PullViewHolder(View itemView) {
        super(itemView);
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, itemView, getAdapterPosition(), 0);
            }
        });
    }
}
