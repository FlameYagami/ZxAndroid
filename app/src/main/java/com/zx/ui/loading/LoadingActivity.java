package com.zx.ui.loading;

import android.Manifest;
import android.os.Build;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zx.R;
import com.zx.ui.base.BaseExActivity;
import com.zx.ui.main.MainActivity;
import com.zx.uitls.AppManager;
import com.zx.uitls.FileUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.LogUtils;
import com.zx.uitls.ZipUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zx.uitls.PathManager.databasePath;
import static com.zx.uitls.PathManager.pictureCache;
import static com.zx.uitls.PathManager.pictureZipPath;

/**
 * Created by 八神火焰 on 2016/12/12.
 */

public class LoadingActivity extends BaseExActivity
{
    @BindView(R.id.view_content)
    RelativeLayout viewContent;
    @BindView(R.id.prg_loading)
    ProgressBar    prgLoading;
    @BindView(R.id.prg_hint)
    TextView       prgHint;
    @BindView(R.id.img_bg_loading)
    ImageView      imgBg;

    private static final String TAG = LoadingActivity.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

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
                    AppManager.getInstances().AppExit(this);
                });
            }
        });
    }

    /**
     * 初始化数据库
     */
    private void initData() {
        Observable.just(this).observeOn(Schedulers.io()).subscribe(activity ->
        {
            // 拷贝资源文件
            FileUtils.copyAssets("data.db", databasePath, false);
            FileUtils.copyAssets("picture.zip", pictureZipPath, false);
            // 解压资源文件到外部
            UnZipPicture();
        }, throwable -> {
            LogUtils.e(TAG, throwable.getMessage());
        });
    }

    private void UnZipPicture() {
        ZipUtils.UnZipFolder("picture.zip", pictureCache).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>()
        {
            @Override
            public void onCompleted() {
                IntentUtils.gotoActivityAndFinish(LoadingActivity.this, MainActivity.class);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(Integer progress) {
                if (-1 != progress) {
                    prgLoading.setProgress(progress);
                    prgHint.setText(String.format("%s/100", progress));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
