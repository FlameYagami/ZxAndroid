package com.zx.ui.result;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.detail.DetailActivity;
import com.zx.uitls.IntentUtils;
import com.zx.view.widget.AppBarView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class ResultActivity extends BaseActivity
{
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    @BindView(R.id.viewAppBar)
    AppBarView   viewAppBar;
    @BindString(R.string.result_about_title)
    String       title;

    ResultAdapter resultAdapter;
    public static List<CardBean> cardBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        viewAppBar.setTitleText(title + String.format("：%s", cardBeanList.size()));
        resultAdapter = new ResultAdapter(this);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        rvResult.setAdapter(resultAdapter);
        resultAdapter.updateData(cardBeanList);
        resultAdapter.setOnItemClickListener((view, data, position) -> {
            DetailActivity.cardBean = cardBeanList.get(position);
            IntentUtils.gotoActivity(this, DetailActivity.class);
        });
    }
}
