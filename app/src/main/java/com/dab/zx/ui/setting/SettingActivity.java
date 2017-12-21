package com.dab.zx.ui.setting;

import com.dab.zx.R;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.view.widget.AppBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class SettingActivity extends BaseActivity {
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

    @OnClick(R.id.sel_advance)
    public void onAdvance_Click() {
        IntentUtils.gotoActivity(this, AdvancedActivity.class);
    }

    @OnClick(R.id.sel_document)
    public void onDocument_Click() {
        IntentUtils.gotoActivity(this, DocumentActivity.class);
    }

    @OnClick(R.id.sel_about)
    public void onAbout_Click() {
        IntentUtils.gotoActivity(this, AboutActivity.class);
    }

    @OnClick(R.id.sel_terminology)
    public void onTerminology_Click() {
        IntentUtils.gotoActivity(this, TerminologyActivity.class);
    }
}
