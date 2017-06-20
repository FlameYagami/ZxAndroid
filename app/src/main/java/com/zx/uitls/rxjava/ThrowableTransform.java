package com.zx.uitls.rxjava;

import com.zx.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/6/8.
 * 描述：错误信息转换类
 * 功能：将已知错误信息类型转换成UI中可理解用语
 */

public class ThrowableTransform
{
    public static String apply(Throwable throwable) {
        if (throwable instanceof ConnectException) {
            // 服务器引发的异常
            return context.getString(R.string.network_exception);
        } else if (throwable instanceof SocketTimeoutException) {
            // 无网络引发的异常
            return context.getString(R.string.service_exception);
        } else {
            return throwable.getMessage();
        }
    }
}
