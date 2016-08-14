package com.ishow.ischool.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.business.tabbusiness.TabBusinessModel;
import com.ishow.ischool.common.manager.JumpManager;

import java.util.List;

/**
 * Created by wqf on 16/8/14.
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    Context mContext;
    List<TabBusinessModel.TabSpec> tabSpecs;

    public BusinessAdapter(Context ctx, List<TabBusinessModel.TabSpec> tabSpecs) {
        mContext = ctx;
        this.tabSpecs = tabSpecs;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_business, parent, false);
        return new BusinessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, final int position) {
        holder.itemTv.setText(tabSpecs.get(position).text);
        holder.itemTv.setCompoundDrawablesWithIntrinsicBounds(0, tabSpecs.get(position).iconResId, 0, 0);
        holder.itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpManager.jumpActivity(mContext, tabSpecs.get(position).intentClazz);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabSpecs.size();
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        TextView itemTv;

        public BusinessViewHolder(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView.findViewById(R.id.business_item);
        }
    }
}
