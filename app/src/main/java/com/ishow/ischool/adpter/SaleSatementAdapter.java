package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by MrS on 2016/9/27.
 */

public class SaleSatementAdapter extends RecyclerView.Adapter<SaleSatementAdapter.ViewHolder> {
    private Context context;
    private List<String> list;

    public SaleSatementAdapter(Context context, List<String> list) {
        this.context = context;

        this.list = list;
    }

    @Override
    public SaleSatementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_salestatement_list_item_tv, parent, false));
    }

    @Override
    public void onBindViewHolder(SaleSatementAdapter.ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    public void onRefresh(List<String> newList) {
      // if (list!=null)list.clear();
        list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.salestatement_item);
        }
    }
}
