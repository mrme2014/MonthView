package monthview.ishow.com.monthview.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by MrS on 2016/8/10.
 */
public abstract class BasicAdapter<T> extends android.widget.BaseAdapter {
    public Context context;
    public List<T> datas;

    public BasicAdapter(Context context, List<T> datas){
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas==null?null:datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getContentView(position,convertView,parent);
    }

    public abstract View getContentView(int position, View convertView, ViewGroup parent);
}
