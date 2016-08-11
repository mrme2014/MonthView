package com.commonlib.core.util;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wqf on 16/8/11.
 */
public class GenericUtil {

    /**
     * @param o 泛型子类
     * @param i 参数顺序
     * @param <T> 泛型
     * @return      泛型对象
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
}
