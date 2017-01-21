package com.zx.ui.setting;

import com.zx.R;
import com.zx.ui.about.AboutActivity;
import com.zx.ui.advanced.AdvancedActivity;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.document.DocumentActivity;
import com.zx.uitls.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class SettingActivity extends BaseActivity
{
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }

    @OnClick(R.id.tv_advanced)
    public void onAdvanced_Click() {
        IntentUtils.gotoActivity(this, AdvancedActivity.class);
    }

    @OnClick(R.id.tv_document)
    public void onDocument_Click() {
        IntentUtils.gotoActivity(this, DocumentActivity.class);
    }

    @OnClick(R.id.tv_about)
    public void onAbout_Click() {
        IntentUtils.gotoActivity(this, AboutActivity.class);
    }

}
