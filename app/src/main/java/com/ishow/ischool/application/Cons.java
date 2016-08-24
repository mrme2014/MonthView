package com.ishow.ischool.application;

/**
 * Created by wqf on 16/8/23.
 */
public class Cons {

    public enum Position {
        None,

        Dongshizhang,
        Zongcai,
        Fuzongcai,
        Xingzhengzhuguan,
        Renshizhuguan,
        Caiwuzhuguan,
        Shichangbuzhang,
        Jiaoxuebuzhang,
        Xiaozhang,
        Shichangfubuzhang,

        Shichangzongjian,
        Shichangzhuguan,
        Xiaojiangshizhuguan,
        Kechengguwenzhuguan,
        Xiaoliaozhuguan,
        Xiaoyuanjingli,
        Chendujiangshi,
        Xiaojiangshi,
        Kechengguwen,
        Xiaoliaozhuanyuan,

        Jiaoxuefubuzhang,
        Jiaoxuezongjian,
        Chujizuzhang,
        Zhongjizuzhang,
        Gaojizuzhang,
        Yingshizuzhang,
        Chujilaoshi,
        Zhongjilaoshi,
        Gaojilaoshi,
        Yingshilaoshi,

        Chujixuexiguwen,
        Zhongjixuexiguwen,
        Gaojixuexiguwen,
        Yingshixuexiguwen
        }

    public interface Communication {
        int source = 1;
    }
}
