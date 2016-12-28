package monthview.ishow.com.monthview.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wqf on 16/4/29.
 * 关于RecycleView中的位置  http://www.jianshu.com/p/aa3b4083edce
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果notifyDataSetChanged()已经被调用而且还没计算新布局，这些方法或许不能够计算适配器位置。
                // 所以，你要小心处理这些方法返回NO_POSITION和null的情况。
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClick(v, getAdapterPosition());
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemLongClick(view, getAdapterPosition());
                }
                return false;
            }
        });
    }

    public abstract void onBindViewHolder(int position);
    public void onItemClick(View view, int position) {

    };
    public void onItemLongClick(View view, int position) {

    };
}
