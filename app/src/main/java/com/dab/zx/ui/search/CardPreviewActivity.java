package com.dab.zx.ui.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dab.zx.R;
import com.dab.zx.bean.CardBean;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.view.widget.AppBarView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class CardPreviewActivity extends BaseActivity {
    @BindView(R.id.rv_result)
    RecyclerView rvCardPreview;
    @BindView(R.id.viewAppBar)
    AppBarView   viewAppBar;
    @BindString(R.string.card_preview_about_title)
    String       title;

    CardPreviewAdapter cardPreviewAdapter;
    public static List<CardBean> cardBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_preview;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        viewAppBar.setTitleText(title + String.format("：%s", cardBeanList.size()));
        cardPreviewAdapter = new CardPreviewAdapter(this);
        rvCardPreview.setLayoutManager(new LinearLayoutManager(this));
        rvCardPreview.setAdapter(cardPreviewAdapter);
        cardPreviewAdapter.updateData(cardBeanList);
        cardPreviewAdapter.setOnItemClickListener((view, data, position) -> {
            CardDetailActivity.cardBean = cardBeanList.get(position);
            IntentUtils.gotoActivity(this, CardDetailActivity.class);
        });
    }
}
