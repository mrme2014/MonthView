package com.ishow.ischool.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.business.tabbusiness.TabBusinessModel;
import com.ishow.ischool.common.manager.JumpManager;

import java.util.List;

/**
 * Created by wqf on 16/8/14.
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    Context mContext;
    List<TabBusinessModel.TabSpec> tabSpecs;
    private int childHeight;

    public BusinessAdapter(Context ctx, List<TabBusinessModel.TabSpec> tabSpecs) {
        mContext = ctx;
        this.tabSpecs = tabSpecs;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_business, parent, false);


        RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            int cloum = (getItemCount() + 1) / spanCount;
            int height = parent.getHeight() - 1;    // 减去分隔线的宽度
            childHeight = height / cloum;
        }
        BusinessViewHolder holder = new BusinessViewHolder(itemView);
        if (childHeight > 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childHeight);
            holder.itemView.setLayoutParams(params);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, final int position) {
        holder.itemTv.setText(tabSpecs.get(position).text);
        holder.itemTv.setCompoundDrawablesWithIntrinsicBounds(0, tabSpecs.get(position).iconResId, 0, 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] resources;
                switch (position) {

                    case 0:
                        resources = new int[]{Resource.MARKET_STUDENT_STATISTICS};
                        break;
                    case 1:
                        resources = new int[]{Resource.SHARE_COMMUNICATION_INDEXM};
                        break;
                    case 2:
                        resources = new int[]{Resource.MARKET_STUDENT_ADD};
                        break;
                    case 3:
                        resources = new int[]{Resource.SHARE_COMMUNICATION_ADDM, Resource.SHARE_COMMUNICATION_ADDM_1};
                        break;
                    default:
                        resources = new int[]{Resource.NO_NEED_CHECK};
                        break;
                }
                if (JumpManager.checkUserPermision(mContext, resources)) {
                    Intent intent = new Intent(mContext, tabSpecs.get(position).intentClazz);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabSpecs.size();
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        TextView itemTv;
        View itemView;

        public BusinessViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemTv = (TextView) itemView.findViewById(R.id.business_item);
        }
    }
}
