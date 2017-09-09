package com.zx.ui.setting;

import com.zx.R;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.loading.LoadingActivity;
import com.zx.uitls.AppManager;
import com.zx.uitls.BundleUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.SystemUtils;
import com.zx.view.dialog.DialogConfirm;
import com.zx.view.widget.AppBarView;
import com.zx.view.widget.MessageView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AboutActivity extends BaseActivity
{
    @BindView(R.id.msg_version)
    MessageView viewVersion;
    @BindView(R.id.viewAppBar)
    AppBarView  viewAppBar;

    @BindString(R.string.resources_repair_confirm)
    String strResourceRepairConfirm;

    private static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        viewVersion.setValue(SystemUtils.getVersionName());
    }

    @OnClick(R.id.msg_resources_repair)
    public void onResourcesRepair() {
        if (DialogConfirm.show(this, strResourceRepairConfirm)) {
            AppManager.finishActivity();
            IntentUtils.gotoActivity(this, LoadingActivity.class, BundleUtils.putBoolean(AboutActivity.class.getSimpleName(), true));
        }
    }

//    @OnClick(R.id.msg_update)
//    public void onCheckUpdate(){
//        RequestApi.checkUpdate().subscribe(mUpdateBean -> {
//            LogUtils.e(TAG, JsonUtils.serializer(mUpdateBean));
//        }, throwable -> {
//            LogUtils.e(TAG,throwable.getMessage());
//        });
//    }
}
