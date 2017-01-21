package com.zx.ui.document;

import com.zx.R;
import com.zx.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class DocumentActivity extends BaseActivity
{
    @Override
    public int getLayoutId() {
        return R.layout.activity_document;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }
}
