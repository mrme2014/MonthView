package com.ishow.ischool.business.registrationform;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BasicAdapter;
import com.ishow.ischool.adpter.ViewHolder;
import com.ishow.ischool.bean.registrationform.CheapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/11/23.
 */

public class CheapListDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView cheapList;
    TextView cancel;

    private ArrayList<CheapType> cheapTypeList;

    OnItemClickListener listener;
    private View contentView;

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnItemClickListener {
        void OnItemClickListener(String cheapTypeTitle, int cheapType, double cheapTypePrice, int cheapTypeId);
    }

    public void setOnItemClickListener(OnItemClickListener listener1) {
        listener = listener1;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.Select_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.activity_registration_cheap_dialog_fragment, null);
        dialog.setContentView(contentView);
        Bundle bundle = getArguments();
        cheapTypeList = bundle.getParcelableArrayList("cheap_list");
        cheapList = (ListView) contentView.findViewById(R.id.cheap_list);
        cancel = (TextView) contentView.findViewById(R.id.cheap_cancel);
        cancel.setOnClickListener(this);
        cheapListAdapter adapter = new cheapListAdapter(getContext(), cheapTypeList);

        cheapList.setAdapter(adapter);
        cheapList.setOnItemClickListener(this);
        Window window = dialog.getWindow();
        window.setLayout(-1, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            CheapType cheapType = cheapTypeList.get(position);
            listener.OnItemClickListener(cheapType.name,
                    Integer.valueOf(cheapType.preferential_type),
                    Double.parseDouble(cheapType.preferential),
                    cheapType.id);
        }
        this.dismiss();
    }

    class cheapListAdapter extends BasicAdapter<CheapType> {

        public cheapListAdapter(Context context, List<CheapType> datas) {
            super(context, datas);
        }

        @Override
        public View getContentView(int position, View convertView, ViewGroup parent) {
            CheapType cheapType = datas.get(position);
            ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.activity_registration_cheap_way_list_item, position);
            ((TextView) holder.getView(R.id.cheap_title)).setText(cheapType.name);
            TextView detail = (TextView) holder.getView(R.id.cheap_detail);

            SpannableString string = null;
            if (TextUtils.equals("1", cheapType.preferential_type)) {
                detail.setText(getString(R.string.registration_cheap_type_dazhe));
                string = new SpannableString("    " + cheapType.preferential + "折");
            } else if (TextUtils.equals("2", cheapType.preferential_type)) {
                detail.setText(getString(R.string.registration_cheap_type_jianmian));
                string = new SpannableString("    " + cheapType.preferential + "元");
            }
            string.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_orange)), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            detail.append(string);
            detail.append("    " + getString(R.string.registration_cheap_time_invalide) + DateUtil.parseSecond2Str(Long.valueOf(cheapType.end_time)));
            return holder.getConvertView();
        }
    }
}
