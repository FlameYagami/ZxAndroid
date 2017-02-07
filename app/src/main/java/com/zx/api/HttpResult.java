package com.zx.api;

/**
 * Created by 时空管理局 on 2016/8/4.
 */
class HttpResult<T>
{
    private int    code;
    private String reason;
    private T      result;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public T getResult()
    {
        return result;
    }

    public void setResult(T result)
    {
        this.result = result;
    }
}
