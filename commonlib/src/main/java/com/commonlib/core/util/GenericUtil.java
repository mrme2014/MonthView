package com.commonlib.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wqf on 16/8/11.
 */
public class GenericUtil {

    /**
     * @param o   泛型子类
     * @param i   参数顺序
     * @param <T> 泛型
     * @return 泛型对象
     * 获取泛型参数
     */
    public static <T> T getType(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Type getGenericClass(Object o) {
        try {
            return ((ParameterizedType) o.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}