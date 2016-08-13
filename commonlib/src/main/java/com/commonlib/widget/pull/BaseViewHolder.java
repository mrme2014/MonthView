package com.commonlib.widget.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wqf on 16/4/29.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });
    }

    public abstract void onBindViewHolder(int position);
    public void onItemClick(View view, int position) {

    };
    public void onItemLongClick(View view, int position) {

    };
}
