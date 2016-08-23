package com.ishow.ischool.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by abel on 16/8/22.
 */
public class ClazzUtil {

    public static void setValue(Map map, Object thisObj) {
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            Object val = map.get(obj);
            setMethod(obj, val, thisObj);
        }
    }

    public static void setMethod(Object method, Object value, Object thisObj) {
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            String met = (String) method;
            met = met.trim();
            if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
                met = met.substring(0, 1).toUpperCase() + met.substring(1);
            }
            if (!String.valueOf(method).startsWith("set")) {
                met = "set" + met;
            }
            Class types[] = new Class[1];
            types[0] = Class.forName("java.lang.String");
            Method m = c.getMethod(met, types);
            m.invoke(thisObj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map getValue(Object thisObj) {
        Map map = new HashMap();
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get") && !method.equals("getClass")) {
                    Object value = m[i].invoke(thisObj);
                    if (value != null) {
                        String key = method.substring(3);
                        key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
