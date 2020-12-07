package com.qq.readbook.net.ok;

/**
 * $desc$
 * author  黄其强
 * Created by Administrator on 2016/12/14 15:40.
 */

public interface HttpCompat {
    interface ParamsCompat {//参数的通用性上还是有些问题
        ParamsCompat put(String key, Object value);
        String paramGet();
        <T> T paramForm();
        <T> T paramMulti();
        String toString();
    }
    HttpCompat init();//init应该没问题了

    void get(String url, ParamsCompat params, OkNetCallback callback);
    void post(String url, ParamsCompat params, OkNetCallback callback);
}
