package com.zx.ui.about;

import com.zx.R;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.SystemUtils;
import com.zx.view.widget.MessageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AboutActivity extends BaseActivity
{
    @BindView(R.id.view_version)
    MessageView viewVersion;

    private static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewVersion.setMessage(SystemUtils.getVersionName());
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }

//    @OnClick(R.id.tv_check_update)
//    public void onCheckUpdate(){
//        RequestApi.checkUpdate().subscribe(mUpdateBean -> {
//            LogUtils.e(TAG, JsonUtils.serializer(mUpdateBean));
//        }, throwable -> {
//            LogUtils.e(TAG,throwable.getMessage());
//        });
//    }
}
