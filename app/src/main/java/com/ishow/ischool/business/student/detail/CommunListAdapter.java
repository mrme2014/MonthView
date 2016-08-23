package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationItem;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abel on 16/8/20.
 */
public class CommunListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<CommunicationItem> datas;
    private LayoutInflater inflater;
    private View.OnClickListener mListener;
    private View.OnClickListener listener;

    public CommunListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public CommunicationItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CommunicationItem.TYPE_COMMUNICSTION_LATEST:
                return new CommunLatestHolder(inflater.inflate(R.layout.item_commun_list_last, parent, false));
            case CommunicationItem.TYPE_COMMUNICSTION_ADD:
                return new CommunAddHolder(inflater.inflate(R.layout.item_commun_add, parent, false));
            case CommunicationItem.TYPE_COMMUNICSTION_CONTENT:
                return new CommunContentHolder(inflater.inflate(R.layout.item_commun_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommunicationItem item = datas.get(position);
        switch (item.type) {
            case CommunicationItem.TYPE_COMMUNICSTION_LATEST: {
                processLatest((CommunLatestHolder) holder, item.communication);
            }
            break;
            case CommunicationItem.TYPE_COMMUNICSTION_ADD: {
                processAdd((CommunAddHolder) holder, item.communication);
            }
            break;
            case CommunicationItem.TYPE_COMMUNICSTION_CONTENT: {
                processContent((CommunContentHolder) holder, item.communication);
            }
            break;
        }
    }

    private void processAdd(CommunAddHolder holder, Communication communication) {
        if (mListener != null) {
            holder.communAddTv.setOnClickListener(mListener);

        }
    }

    private void processContent(CommunContentHolder holder, Communication communication) {
        holder.communDateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.update_time * 1000, "yyyy-MM-dd"));
        holder.communContentTv.setText(communication.communicationInfo.content);
        holder.opraterNameTv.setText(communication.userInfo.user_name);
    }

    private void processLatest(CommunLatestHolder holder, Communication communication) {
        holder.communStateTv.setText(AppUtil.getStateById(communication.communicationInfo.status));
        holder.faithTv.setText(AppUtil.getBeliefById(communication.communicationInfo.belief));
        holder.opposeTv.setText(AppUtil.getRefuseById(communication.communicationInfo.refuse));
        holder.sourceTv.setText(communication.communicationInfo.tuition_source);
        holder.backDateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.callback_date * 1000, "yyyy-MM-dd"));

        if (mListener != null) {
            holder.communStateTv.setOnClickListener(mListener);
            holder.faithTv.setOnClickListener(mListener);
            holder.opposeTv.setOnClickListener(mListener);
            holder.sourceTv.setOnClickListener(mListener);
            holder.backDateTv.setOnClickListener(mListener);
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void refresh(ArrayList<CommunicationItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void refreshAdd(ArrayList<CommunicationItem> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    class CommunLatestHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commun_state)
        LabelTextView communStateTv;

        @BindView(R.id.commun_faith)
        LabelTextView faithTv;

        @BindView(R.id.commun_oppose)
        LabelTextView opposeTv;

        @BindView(R.id.commun_source)
        LabelTextView sourceTv;

        @BindView(R.id.commun_back_date)
        LabelTextView backDateTv;

        public CommunLatestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (listener != null) {
                communStateTv.setOnClickListener(listener);
                faithTv.setOnClickListener(listener);
                opposeTv.setOnClickListener(listener);
                sourceTv.setOnClickListener(listener);
                backDateTv.setOnClickListener(listener);
            }
        }


    }

    class CommunAddHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_commun_add_btn)
        TextView communAddTv;

        public CommunAddHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (listener != null) {
                communAddTv.setOnClickListener(listener);
            }
        }
    }

    class CommunContentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_commun_date)
        TextView communDateTv;

        @BindView(R.id.item_commun_content)
        TextView communContentTv;

        @BindView(R.id.item_oprater_name)
        TextView opraterNameTv;


        public CommunContentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
