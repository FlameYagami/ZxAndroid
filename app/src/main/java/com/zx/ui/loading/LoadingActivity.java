package com.zx.ui.loading;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zx.R;
import com.zx.ui.base.BaseExActivity;
import com.zx.ui.main.MainActivity;
import com.zx.ui.setting.AboutActivity;
import com.zx.uitls.AppManager;
import com.zx.uitls.FileUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.ZipUtils;
import com.zx.uitls.rxjava.ResourcesSubscriber;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.zx.uitls.PathManager.banlistPath;
import static com.zx.uitls.PathManager.databasePath;
import static com.zx.uitls.PathManager.pictureCache;
import static com.zx.uitls.PathManager.pictureZipPath;

/**
 * Created by 八神火焰 on 2016/12/12.
 */

public class LoadingActivity extends BaseExActivity
{
    @BindView(R.id.viewContent)
    RelativeLayout viewContent;
    @BindView(R.id.prg_loading)
    ProgressBar    prgLoading;
    @BindView(R.id.prg_hint)
    TextView       prgHint;
    @BindView(R.id.img_bg_loading)
    ImageView      imgBg;

    private static final String TAG = LoadingActivity.class.getSimpleName();

    private boolean isResourcesRepair = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            isResourcesRepair = bundle.getBoolean(AboutActivity.class.getSimpleName());
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {
            initData();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                initData();
            } else {
                showSnackBar(viewContent, "权限不足,程序将在2s后关闭");
                Observable.interval(2000, TimeUnit.MILLISECONDS).subscribe(along -> {
                    AppManager.AppExit(this);
                });
            }
        });
    }

    /**
     * 初始化数据库
     */
    private void initData() {
        ResourcesSubscriber.ObservableEx observable0 = new ResourcesSubscriber.ObservableEx(FileUtils.copyAssets("data.db", databasePath, isResourcesRepair), "正在拷贝数据库...");
        ResourcesSubscriber.ObservableEx observable1 = new ResourcesSubscriber.ObservableEx(FileUtils.copyAssets("restrict", banlistPath, isResourcesRepair), "正在拷贝制限卡表...");
        ResourcesSubscriber.ObservableEx observable2 = new ResourcesSubscriber.ObservableEx(FileUtils.copyAssets("picture.zip", pictureZipPath, isResourcesRepair), "正在拷贝图片资源...");
        ResourcesSubscriber.ObservableEx observable3 = new ResourcesSubscriber.ObservableEx(ZipUtils.unZipFolder("picture.zip", pictureCache, isResourcesRepair), "正在解压图片资源");
        new ResourcesSubscriber(observable0, observable1, observable2, observable3).apply(prgLoading, prgHint)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResourcesSubscriber.ObservableEx>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResourcesSubscriber.ObservableEx observableEx) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        IntentUtils.gotoActivityAndFinish(LoadingActivity.this, MainActivity.class);
                    }
                });
    }

}
