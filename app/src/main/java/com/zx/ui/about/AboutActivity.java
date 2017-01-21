package com.zx.ui.about;

import android.widget.TextView;

import com.zx.R;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AboutActivity extends BaseActivity
{
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        tvVersion.setText(SystemUtils.getVersionName());
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }
}
