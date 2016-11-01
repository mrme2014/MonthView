package com.ishow.ischool.business.classattention;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.classattend.ClazStudentObject;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.util.PicUtils;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.CommunEditDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MrS on 2016/10/20.
 */

public class ClazListAdapter extends RecyclerView.Adapter<ClazListAdapter.ClazViewHolder> {

    private Context context;
    private List<ClazStudentObject> lists;
    private HashMap<Integer,String> beiZhuList;
    public HashMap<Integer,Boolean> checkInList;
    private AdapterView.OnClickListener listener;

    public ClazListAdapter(Context context, List<ClazStudentObject> lists) {

        this.context = context;
        this.lists = lists;
       /* lists.addAll(lists);
        lists.addAll(lists);
        lists.addAll(lists);
        lists.addAll(lists);*/

        beiZhuList = new HashMap<>();
        checkInList = new HashMap<>();
        for (int i = 0; i <lists.size() ; i++) {
            checkInList.put(i,true);
            beiZhuList.put(i,"");
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public ClazViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClazViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_class_attendace_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(final ClazListAdapter.ClazViewHolder holder, final int position) {
        StudentInfo studentInfo = lists.get(position).studentInfo;
        if (studentInfo == null)
            return;
        if (studentInfo.avatar != null && studentInfo.avatar != ""&&studentInfo.avatar != "0") {
            PicUtils.loadpic(context, holder.avartImg, studentInfo.avatar);
        } else {
            holder.avartImg.setVisibility(View.GONE);
            holder.avartTx.setVisibility(View.VISIBLE);
            holder.avartTx.setText(studentInfo.name,studentInfo.id,studentInfo.avatar);
        }
        holder.name.setText(studentInfo.name);
        if (beiZhuList.get(position)!=null&&beiZhuList.get(position)!=""){
            holder.beiZhu.setBackgroundResource(R.drawable.bg_round_corner_blue);
            holder.beiZhu.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            holder.beiZhu.setBackgroundResource(R.drawable.bg_round_corner_gray);
            holder.beiZhu.setTextColor(context.getResources().getColor(R.color.txt_9));
        }
        holder.button.setChecked(checkInList.get(position));
        holder.button.setText(checkInList.get(position)?"上课":"缺勤");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.button.setText(checkInList.get(position)?"缺勤":"上课");
                checkInList.put(position,holder.button.isChecked());
            }
        });
        holder.beiZhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunEditDialog dialog = new CommunEditDialog();
                Bundle bundle = new Bundle();
                bundle.putString("content",beiZhuList.get(position));
                bundle.putBoolean("needDate",false);
                dialog.setArguments(bundle);
                dialog.show(((ClassAttendActivity)context).getSupportFragmentManager(),"dialog");
                dialog.setOnClickListener(new CommunEditDialog.OnClickListener() {
                    @Override
                    public void onClick(String content, long date) {
                        holder.beiZhu.setBackgroundResource(R.drawable.bg_round_corner_blue);
                        holder.beiZhu.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        beiZhuList.put(position,content);
                    }
                });
            }
        });
    }

    public void setOnItemClickListner(AdapterView.OnClickListener listener1){
        this.listener = listener1;
    }
    public void refreshAdapter(List<ClazStudentObject> newDatas) {
        this.lists = newDatas;
        notifyDataSetChanged();
    }

    public int getCheckInState(int position){
        return checkInList.get(position)?1:2;
    }
    public String getCheckBeiZhuContent(int position){
        return beiZhuList.get(position);
    }
    class ClazViewHolder extends RecyclerView.ViewHolder {
        public Switch button;
        public TextView beiZhu;
        public TextView name;
        public CircleImageView avartImg;
        public AvatarImageView avartTx;

        public ClazViewHolder(final View itemView) {
            super(itemView);
            this.button = (Switch) itemView.findViewById(R.id.class_togbtn);
            this.beiZhu = (TextView) itemView.findViewById(R.id.class_beizhu);
            this.name = (TextView) itemView.findViewById(R.id.class_name);
            this.avartImg = (CircleImageView) itemView.findViewById(R.id.item_avart_img);
            this.avartTx = (AvatarImageView) itemView.findViewById(R.id.item_avart_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onClick(itemView);
                }
            });
        }
    }
}
