package monthview.ishow.com.monthview.base;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 15/8/2.
 */
public abstract class BaseListAdapter<T> extends android.widget.BaseAdapter {

    public Context context;

    private LayoutInflater inflater;

    private ArrayList<T> datas;

    public BaseListAdapter() {
    }

    public BaseListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context, ArrayList<T> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public Context getContext() {
        return context;
    }

    public void setDatas(List<T> list) {
        if (list == null) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList<>();
        } else {
            datas.clear();
        }
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void appendDatas(List<T> list) {
        if (list == null) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int postion) {
        datas.remove(postion);
        notifyDataSetChanged();
    }

    public ArrayList<T> getDatas() {
        return datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
