package com.dab.zx.ui.setting;

import com.dab.zx.R;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.ui.loading.LoadingActivity;
import com.dab.zx.uitls.AppManager;
import com.dab.zx.uitls.BundleUtils;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.SystemUtils;
import com.dab.zx.view.dialog.DialogConfirm;
import com.dab.zx.view.widget.AppBarView;
import com.dab.zx.view.widget.MessageView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AboutActivity extends BaseActivity {
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
