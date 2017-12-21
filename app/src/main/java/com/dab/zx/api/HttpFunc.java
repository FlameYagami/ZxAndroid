package com.dab.zx.api;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by 时空管理局 on 2016/8/4.
 */
class HttpFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(@NonNull HttpResult<T> httpResult) throws Exception {
        if (200 != httpResult.getCode()) {
            throw new RuntimeException(httpResult.getCode() + httpResult.getReason());
        }
        return httpResult.getResult();
    }
}


