package com.zx.ui.document;

import com.zx.R;
import com.zx.ui.base.BaseActivity;
import com.zx.view.widget.AppBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class DocumentActivity extends BaseActivity
{
    @BindView(R.id.viewAppBar)
    AppBarView viewAppBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_document;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
    }
}
