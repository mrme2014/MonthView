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

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BasicAdapter;
import com.ishow.ischool.adpter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/8/10.
 */
public class SelectDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private Button cancel;
    private SelectDialogAdapter adapter;
    private Dialog dialog;

    private ArrayList<String> datas; //message btn  文本集合
    private ArrayList<Integer> messageColor;//message btn的颜色集合
    private int buttonColor;//取消按钮的颜色

    /**
     * 使用方法：
     * SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
     * SelectDialogFragment dialog= builder.setMessage("SAS", "ASDAS", "ASDAS")
     * .setMessageColor(R.color.color_header,R.color.colorAccent,R.color.green_press).Build();
     * dialog.show(getChildFragmentManager());
     * <p/>
     * OR:
     * SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
     * SelectDialogFragment dialog= builder.setMessage(list<String> datas)
     * .setMessageColor(List<Integer> colors).Build();
     * dialog.show(getChildFragmentManager());
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), R.style.Select_dialog);
        View contentView = View.inflate(getContext(), R.layout.select_dialog, null);
        listView = (ListView) contentView.findViewById(R.id.select_dialog_content);
        listView.setBackgroundResource(R.drawable.bg_round_corner);
        cancel = (Button) contentView.findViewById(R.id.select_dialog_cancel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.datas = bundle.getStringArrayList("datas");
            this.messageColor = bundle.getIntegerArrayList("messageColor");
            this.buttonColor = bundle.getInt("buttonColor");
        }
        adapter = new SelectDialogAdapter(getContext(), datas, messageColor);
        listView.setAdapter(adapter);

        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params1 = window.getAttributes();
        params1.gravity = Gravity.BOTTOM;
        params1.y = UIUtil.dip2px(getContext(), 10);
        window.setAttributes(params1);

        listView.setOnItemClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listner != null)
            listner.onItemSelected(position);
        this.dismiss();
    }

    public void show(FragmentManager manger) {
        show(manger, "dialog");
    }

    public static class Builder {
        private int buttonColor;//取消按钮的颜色
        public ArrayList<String> datas;//message btb 文本集合
        public ArrayList<Integer> colors; //为每一个message btb指定不同的颜色
        private OnItemSelectedListner listner;


        public int getButtonColor() {
            return buttonColor;
        }

        /*===========setMessage(String... args)========setMessage(List<String> args)使用其中一个就可以了======================================*/
        public Builder setMessage(String... args) {
            if (datas == null) datas = new ArrayList<>();
            datas.clear();
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    datas.add(args[i]);
                }
            }
            return this;
        }

        public Builder setMessage(List<String> args) {
            if (datas == null) datas = new ArrayList<>();
            datas.clear();
            if (args != null) datas.addAll(args);
            return this;
        }

        /*===========setMessageColor(int...messageColor)========setMessageColor(int messageColor)使用其中一个就可以了======================================*/
        public Builder setMessageColor(int... messageColor) {
            if (colors == null) colors = new ArrayList<>();
            colors.clear();
            for (int i = 0; i < messageColor.length; i++) {
                colors.add(messageColor[i]);
            }
            return this;
        }

        public Builder setMessageColor(int messageColor) {
            if (colors == null) colors = new ArrayList<>();
            colors.clear();
            colors.add(messageColor);
            return this;
        }

        public Builder setButtonColor(int buttonColor) {
            this.buttonColor = buttonColor;
            return this;
        }

        public Builder setOnItemselectListner(OnItemSelectedListner l) {
            this.listner = l;
            return this;
        }

        public SelectDialogFragment Build() {

            SelectDialogFragment fragment = new SelectDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("datas", datas);
            bundle.putIntegerArrayList("messageColor", colors);
            bundle.putInt("buttonColor", buttonColor);
            fragment.setArguments(bundle);
            fragment.setOnItemSelectedListner(listner);
            return fragment;
        }
    }

    class SelectDialogAdapter extends BasicAdapter<String> {

        private ArrayList<Integer> messageColor;

        public SelectDialogAdapter(Context context, List<String> datas, ArrayList<Integer> messageColor) {
            super(context, datas);
            this.messageColor = messageColor;
        }

        @Override
        public View getContentView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.select_dialog_item, position);
            TextView item = holder.getView(R.id.select_dialog_listview_item);
            item.setText(datas.get(position));

            /*这种情况是 为每一个 message btn指定一个颜色 messageColor的大小 就会跟datas集合大小一样*/

            if (messageColor != null && messageColor.size() >= datas.size()) {
                item.setTextColor(context.getResources().getColor(messageColor.get(position)));
            }
                /*下面这 种情况是 为 message btn指定一个颜色 messageColor的大小 就会小于datas集合大小
                * 这种情况超过messageColor的大小的位置 会使用默认颜色 */
            else if (messageColor != null && messageColor.size() <= datas.size()) {
                if (position < messageColor.size())
                    item.setTextColor(context.getResources().getColor(messageColor.get(position)));
            }

            return holder.getConvertView();
        }
    }

    public interface OnItemSelectedListner {
        public void onItemSelected(int position);
    }

    private OnItemSelectedListner listner;

    public void setOnItemSelectedListner(OnItemSelectedListner l) {
        this.listner = l;
    }
}
