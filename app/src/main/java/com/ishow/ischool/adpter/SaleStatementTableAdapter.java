package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.ischool.R;
import com.ishow.ischool.widget.custom.TableRowTextView;

import java.util.List;

/**
 * Created by MrS on 2016/9/14.
 */
public class SaleStatementTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<List<String>> list;

    public SaleStatementTableAdapter(Context context, List<List<String>> list) {
        this.context = context;

        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SaleHolder(LayoutInflater.from(context).inflate(R.layout.activity_salestatement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SaleHolder saleHolder = (SaleHolder) holder;
        saleHolder.rowTextView.setTxtList(list.get(position));
        if (position >= getItemCount() - 1) saleHolder.rowTextView.setShouldDrawBotLine(true);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateTableBodys(List<List<String>> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    class SaleHolder extends RecyclerView.ViewHolder {
        TableRowTextView rowTextView;

        public SaleHolder(View itemView) {
            super(itemView);
            rowTextView = (TableRowTextView) itemView.findViewById(R.id.sale_table_row);
        }
    }
}
