package com.zx.api;

import rx.functions.Func1;

/**
 * Created by 时空管理局 on 2016/8/4.
 */
class HttpFunc<T> implements Func1<HttpResult<T>, T>
{
    @Override
    public T call(HttpResult<T> httpResult)
    {
        if (200 != httpResult.getCode())
        {
            throw new RuntimeException(httpResult.getCode() + httpResult.getReason());
        }
        return httpResult.getResult();
    }
}


