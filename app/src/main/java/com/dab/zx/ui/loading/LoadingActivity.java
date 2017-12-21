package com.dab.zx.ui.loading;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dab.zx.R;
import com.dab.zx.ui.base.BaseExActivity;
import com.dab.zx.ui.main.MainActivity;
import com.dab.zx.ui.setting.AboutActivity;
import com.dab.zx.uitls.AppManager;
import com.dab.zx.uitls.FileUtils;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.ZipUtils;
import com.dab.zx.uitls.rxjava.ObservableEx;
import com.dab.zx.uitls.rxjava.ResourcesSubscriber;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.dab.zx.uitls.PathManager.databasePath;
import static com.dab.zx.uitls.PathManager.pictureDir;
import static com.dab.zx.uitls.PathManager.pictureZipPath;
import static com.dab.zx.uitls.PathManager.restrictPath;

/**
 * Created by 八神火焰 on 2016/12/12.
 */

public class LoadingActivity extends BaseExActivity {
    @BindView(R.id.viewContent)
    View        viewContent;
    @BindView(R.id.prg_loading)
    ProgressBar prgLoading;
    @BindView(R.id.prg_hint)
    TextView    prgHint;
    @BindView(R.id.img_bg_loading)
    ImageView   imgBg;

    private static final String TAG = LoadingActivity.class.getSimpleName();

    private boolean isRepair = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        // 资源修复判定
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            isRepair = bundle.getBoolean(AboutActivity.class.getSimpleName());
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
                showSnackBar(viewContent, getString(R.string.exit_with_not_permissions));
                Observable.interval(2000, TimeUnit.MILLISECONDS).subscribe(along -> AppManager.AppExit(this));
            }
        });
    }

    /**
     * 初始化资源
     */
    private void initData() {
        ObservableEx ob0 = new ObservableEx(FileUtils.copyAssets("data.db", databasePath, isRepair), getString(R.string.database_is_copying));
        ObservableEx ob1 = new ObservableEx(FileUtils.copyAssets("restrict", restrictPath, isRepair), getString(R.string.restrict_is_copying));
        ObservableEx ob2 = new ObservableEx(FileUtils.copyAssets("picture.zip", pictureZipPath, isRepair), getString(R.string.image_is_copying));
        ObservableEx ob3 = new ObservableEx(ZipUtils.unZipFolder("picture.zip", pictureDir, isRepair), getString(R.string.image_is_unzipping));
        new ResourcesSubscriber(ob0, ob1, ob2, ob3).apply(prgLoading, prgHint)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ObservableEx>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ObservableEx observableEx) {

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
