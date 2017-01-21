package com.zx.ui.result;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.detail.DetailActivity;
import com.zx.uitls.IntentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class ResultActivity extends BaseActivity
{
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    @BindView(R.id.tv_result_count)
    TextView     tvResultCount;

    ResultAdapter resultAdapter;
    public static List<CardBean> cardBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        tvResultCount.setText(String.format("%s", cardBeanList.size()));
        resultAdapter = new ResultAdapter(this);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        rvResult.setAdapter(resultAdapter);
        resultAdapter.updateData(cardBeanList);
        resultAdapter.setOnItemClickListener((view, data, position) -> {
            DetailActivity.cardBean = cardBeanList.get(position);
            IntentUtils.gotoActivity(this, DetailActivity.class);
        });
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }
}
