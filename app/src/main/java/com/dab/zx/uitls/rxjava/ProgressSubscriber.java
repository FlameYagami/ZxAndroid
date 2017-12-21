package com.dab.zx.uitls.rxjava;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.dab.zx.view.dialog.DialogLoadingUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 八神火焰 on 2017/6/6.
 * 描述：Http请求等待提示框封装类
 * 功能：网络请求时自动显示及隐藏等待提示框,同时支持返回键取消网络请求
 */

public class ProgressSubscriber<T> {
    private Context       mContext;
    private Observable<T> mObservable;
    private Disposable    mDisposable;
    private String        mMessage;

    public ProgressSubscriber(Context mContext, Observable<T> mObservable) {
        this.mContext = mContext;
        this.mObservable = mObservable;
    }

    public ProgressSubscriber(Context mContext, Observable<T> mObservable, String mMessage) {
        this.mContext = mContext;
        this.mObservable = mObservable;
        this.mMessage = mMessage;
    }

    /**
     * 默认构造函数,等待提示框不可取消
     *
     * @return 被观测对象
     */
    public Observable<T> apply() {
        return apply(false);
    }

    /**
     * 二级构造函数,等待提示框能否取消收参数影响
     *
     * @param cancelable 等待提示框是否可取消
     * @return 被观测对象
     */
    public Observable<T> apply(boolean cancelable) {
        return Observable.create(subscriber -> mObservable.subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                mDisposable = disposable;
                DialogLoadingUtils.show(mContext, mMessage, cancelable ? KeyListener : null);
            }

            @Override
            public void onNext(T t) {
                subscriber.onNext(t);
                onComplete();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(new Throwable(ThrowableTransform.apply(e)));
                onComplete();
            }

            @Override
            public void onComplete() {
                DialogLoadingUtils.hide();
            }
        }));
    }

    /**
     * 返回键的监听
     */
    private DialogInterface.OnKeyListener KeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                DialogLoadingUtils.hide();
                mDisposable.dispose();
            }
            return false;
        }
    };
}