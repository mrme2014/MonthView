package com.ishow.ischool.util;

/**
 * Created by abel on 16/8/25.
 */
public class ColorUtil {

    public static final int[] colors = {
              0xFFCCCC33
            , 0xFFFFFF99
            , 0xFFCC9933
            , 0xFFCC9966
            , 0xFFCCCC66
            , 0xFF669999
            , 0xFFFF9966
            , 0xFF996600
            , 0xFFCCCC00
            , 0xFFCCCC66
            , 0xFF660033
            , 0xFFCC6600
            , 0xFFCCCC00
            , 0xFF666600
            , 0xFFCCCCFF
            , 0xFFCC9933
            , 0xFF009999
            , 0xFFFFCC33
            , 0xFF999966
            , 0xFFCCCC99
            , 0xFF339999
            , 0xFF99CC99
            , 0xFF669933
            , 0xFF336633
            , 0xFF666633
            , 0xFF999933
            , 0xFFCC9966
            , 0xFF660000
            , 0xFFCC9900
            , 0xFFCCCC99};

    public static int getColorById(int id) {
        return colors[id % colors.length];
    }
}
