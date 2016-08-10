package com.ishow.ischool.widget.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BasicAdapter;
import com.ishow.ischool.adpter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/8/10.
 */
public class SelectDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView content;
    private Button cancel;
    private selectDialogAdapter adapter;
    private Dialog dialog;
    private ArrayList<String> datas;
    private int messageColor;
    private int buttonColor;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog = new Dialog(getContext(),R.style.Select_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View contentView = View.inflate(getContext(), R.layout.select_dialog, null);
        content = (ListView) contentView.findViewById(R.id.select_dialog_content);
        content.setBackgroundResource(R.drawable.bg_round_corner);
        cancel = (Button) contentView.findViewById(R.id.select_dialog_cancel);

        Bundle bundle = getArguments();
        if (bundle!=null){
            this.datas = bundle.getStringArrayList("datas");
            this.messageColor = bundle.getInt("messageColor");
            this.buttonColor =  bundle.getInt("buttonColor");
        }
        adapter = new selectDialogAdapter(getContext(), datas ,messageColor);
        content.setAdapter(adapter);

        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params1 = window.getAttributes();
        params1.gravity= Gravity.BOTTOM;
       /* params1.y = UIUtil.getScreenHeightPixels(getContext())-UIUtil.dip2px(getContext(),8);
        LogUtil.e(params1.y+"");*/
        window.setAttributes(params1);

        content.setOnItemClickListener(this);
        cancel.setOnClickListener(this);

        LogUtil.e(datas.toString()+"==="+messageColor);
        return dialog;
    }
    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listner != null)
            listner.onItemselect(position, ((TextView) view).getText().toString());
        this.dismiss();
    }

    public void show(FragmentManager manger){
        show(manger,"dialog");
    }

    public static class Builder {
        private String[] Message;
        private int messageColor;
        private List<String> args;
        public ArrayList<String> datas;

        public int getButtonColor() {
            return buttonColor;
        }

        public int getMessageColor() {
            return messageColor;
        }

        private int buttonColor;

        public Builder setMessage(String... args) {
            Message = args;
            return this;
        }

        public Builder setMessage(List<String> args) {
            this.args = args;
            return this;
        }

        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }

        public Builder setButtonColor(int buttonColor) {
            this.buttonColor = buttonColor;
            return this;
        }

        public SelectDialogFragment Build() {
            datas = new ArrayList<>();
            if (args != null) datas.addAll(args);

            else if (Message != null) {
                for (int i = 0; i < Message.length; i++) {
                    datas.add(Message[i]);
                }
            }
            SelectDialogFragment fragment = new SelectDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("datas",datas);
            bundle.putInt("messageColor",messageColor);
            bundle.putInt("buttonColor",buttonColor);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    class selectDialogAdapter extends BasicAdapter<String> {

        private int messageColor;

        public selectDialogAdapter(Context context, List<String> datas, int messageColor) {
            super(context, datas);
            this.messageColor = messageColor;
        }

        @Override
        public View getContentView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.select_dialog_item, position);
            TextView item = holder.getView(R.id.select_dialog_listview_item);
            item.setText(datas.get(position));
            //item.setTextColor(messageColor);
            return holder.getConvertView();
        }
    }

    public interface onItemselectListner {
        public void onItemselect(int position, String txt);
    }

    public onItemselectListner listner;

    public void addOnItemselectListner(onItemselectListner l) {
        this.listner = l;
    }
}
