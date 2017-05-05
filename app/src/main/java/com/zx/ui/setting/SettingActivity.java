package com.zx.ui.setting;

import com.zx.R;
import com.zx.ui.about.AboutActivity;
import com.zx.ui.advanced.AdvancedActivity;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.document.DocumentActivity;
import com.zx.uitls.IntentUtils;
import com.zx.view.widget.AppBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class SettingActivity extends BaseActivity
{
    @BindView(R.id.viewAppBar)
    AppBarView viewAppBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
    }

    @OnClick(R.id.msg_advance)
    public void onAdvance_Click() {
        IntentUtils.gotoActivity(this, AdvancedActivity.class);
    }

    @OnClick(R.id.msg_document)
    public void onDocument_Click() {
        IntentUtils.gotoActivity(this, DocumentActivity.class);
    }

    @OnClick(R.id.msg_about)
    public void onAbout_Click() {
        IntentUtils.gotoActivity(this, AboutActivity.class);
    }

}
