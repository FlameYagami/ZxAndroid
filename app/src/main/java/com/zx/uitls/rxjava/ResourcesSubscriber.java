package com.zx.uitls.rxjava;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 八神火焰 on 2017/7/26.
 */

public class ResourcesSubscriber
{
    private static volatile int mObservableExecute;

    private Disposable         mDisposable;
    private List<ObservableEx> mObservableList;

    public ResourcesSubscriber(ObservableEx... observableExList) {
        mObservableExecute = 0;
        mObservableList = new ArrayList<>();
        Collections.addAll(mObservableList, observableExList);
    }

    public Observable<ObservableEx> apply(ProgressBar progressBar, TextView textView) {
        return Observable.create(subscriber ->
                mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    // 所有资源释放完毕
                    if (mObservableList.size() == mObservableExecute) {
                        subscriber.onComplete();
                        mDisposable.dispose();
                        mDisposable = null;
                    }

                    // 执行资源文件任务
                    ObservableEx tempObservableEx = mObservableList.get(mObservableExecute);
                    if (!tempObservableEx.isExecute()) {
                        textView.setText(tempObservableEx.getMessage());
                        tempObservableEx.setExecute(true);
                        tempObservableEx.getObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                progressBar::setProgress,
                                subscriber::onError,
                                () -> {
                                    tempObservableEx.setExecute(false);
                                    mObservableExecute++;
                                });
                    }
                }));
    }

    public static class ObservableEx
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
}
