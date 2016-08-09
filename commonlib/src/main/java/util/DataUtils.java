package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by abel on 15/11/10.
 */
public class DataUtils {
    public static void removeDuplicateWithOrder(List list) {
        LogUtil.d("befor list size " + list.size());
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        LogUtil.d("after list size " + list.size());
    }
}
