package com.ishow.ischool.business.student.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.business.communication.detail.CommunicationDetailActivity;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.PicUtils;
import com.ishow.ischool.widget.custom.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abel on 16/8/20.
 */
public class CommunListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Communication> datas;
    private LayoutInflater inflater;

    public CommunListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public Communication getItem(int position) {
        return datas != null ? datas.get(position) : null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunContentHolder(inflater.inflate(R.layout.item_commun_content, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Communication item = datas.get(position);
        processContent((CommunContentHolder) holder, item);
    }

    private void processContent(CommunContentHolder holder, final Communication communication) {
        holder.communDateTv.setText(DateUtil.parseSecond2Str(communication.communicationInfo.communication_date));
        holder.communContentTv.setText(communication.communicationInfo.content);
        holder.opraterNameTv.setText(communication.userInfo.user_name);
        PicUtils.loadAvatarCircle(context, holder.avatarCiv, communication.avatar.file_name);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void refresh(ArrayList<Communication> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    class CommunContentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_commun_date)
        TextView communDateTv;
        @BindView(R.id.item_commun_content_group)
        RelativeLayout layout;
        @BindView(R.id.item_commun_content)
        TextView communContentTv;
        @BindView(R.id.item_oprater_name)
        TextView opraterNameTv;
        @BindView(R.id.item_commun_user_avatar)
        CircleImageView avatarCiv;


        public CommunContentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommunicationDetailActivity.class);
                    intent.putExtra(CommunicationDetailActivity.COMMUNICATION_DATA, datas.get(getAdapterPosition()));
                    JumpManager.jumpActivity(context, intent, Resource.NO_NEED_CHECK);
                }
            });
        }
    }
}
