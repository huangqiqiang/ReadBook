
package com.qq.readbook.net.ok;

/**
 * $desc$
 * author  黄其强
 * Created by Administrator on 2016/12/14 15:51.
 */

public class HOkHttp {
    private static HttpCompat mOkHttp = null;

    public synchronized static HttpCompat newHttpCompat() {
        if (mOkHttp == null) {
            mOkHttp = new OkHttpImpl()
                    .init();
        }
        return mOkHttp;
    }

    public static HttpCompat.ParamsCompat newParamsCompat() {
        return new OKParamsImpl();
    }

    public static HttpCompat.ParamsCompat newParamsCompat(String key, Object values) {
        HttpCompat.ParamsCompat paramsCompat = newParamsCompat();
        paramsCompat.put(key, values);
        return paramsCompat;
    }
}
