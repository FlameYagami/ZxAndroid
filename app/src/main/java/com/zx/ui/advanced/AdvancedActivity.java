package com.zx.ui.advanced;

import android.support.v7.app.AlertDialog;

import com.zx.R;
import com.zx.bean.KeySearchBean;
import com.zx.config.SpConst;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.StringUtils;
import com.zx.view.dialog.DialogCheckBox;
import com.zx.view.widget.AppBarView;
import com.zx.view.widget.MessageView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AdvancedActivity extends BaseActivity
{
    @BindArray(R.array.order)
    String[]    mOrderPatternArrays;
    @BindView(R.id.view_order_pattern)
    MessageView viewOrderPattern;
    @BindView(R.id.view_key_search)
    MessageView viewKeySearch;
    @BindView(R.id.viewAppBar)
    AppBarView  viewAppBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_advanced;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        viewOrderPattern.setDefaultSp(SpConst.OrderPattern, mOrderPatternArrays[0]);
        viewKeySearch.setValue(StringUtils.changeList2String(KeySearchBean.getSelectKeySearchList()));
    }

    @OnClick(R.id.view_order_pattern)
    public void onOrderPattern_Click() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.order_pattern))
                .setItems(mOrderPatternArrays, (view, index) -> viewOrderPattern.setDefaultSp(SpConst.OrderPattern, mOrderPatternArrays[index]))
                .show();
    }

    @OnClick(R.id.view_key_search)
    public void onKeySearch_Click() {
        new DialogCheckBox(this, getString(R.string.key_search), KeySearchBean.getKeySearchMap(), keySearchMap -> {
            KeySearchBean.saveKeySearchMap(keySearchMap);
            viewKeySearch.setValue(StringUtils.changeList2String(KeySearchBean.getSelectKeySearchList()));
        }).show();
    }
}
