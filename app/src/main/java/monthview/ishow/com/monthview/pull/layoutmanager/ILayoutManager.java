package monthview.ishow.com.monthview.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;

import monthview.ishow.com.monthview.pull.BaseListAdapter;


/**
 * Created by wqf on 16/4/29.
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
