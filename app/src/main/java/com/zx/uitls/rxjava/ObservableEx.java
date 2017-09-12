package com.zx.uitls.rxjava;

import io.reactivex.Observable;

/**
 * Created by 八神火焰 on 2017/9/11.
 */

public class ObservableEx
{
    private boolean             isExecute;
    private String              mMessage;
    private Observable<Integer> mObservable;

    public ObservableEx(Observable<Integer> mObservable, String mMessage) {
        this.mObservable = mObservable;
        this.mMessage = mMessage;
        isExecute = false;
    }

    public Observable<Integer> getObservable() {
        return mObservable;
    }

    public boolean isExecute() {
        return isExecute;
    }

    public void setExecute(boolean execute) {
        isExecute = execute;
    }

    public String getMessage() {
        return mMessage;
    }
}
