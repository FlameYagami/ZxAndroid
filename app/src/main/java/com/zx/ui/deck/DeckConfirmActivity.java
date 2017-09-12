package com.zx.ui.deck;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zx.R;
import com.zx.config.SpConst;
import com.zx.game.DeckManager;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.SpUtils;
import com.zx.view.widget.AppBarView;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/9/4.
 */

public class DeckConfirmActivity extends BaseActivity
{
    @BindView(R.id.viewAppBar)
    AppBarView   viewAppBar;
    @BindView(R.id.rv_ig)
    RecyclerView rvIg;
    @BindView(R.id.rv_ug)
    RecyclerView rvUg;
    @BindView(R.id.rv_ex)
    RecyclerView rvEx;
    @BindView(R.id.tv_ig_stats)
    TextView     tvIgStats;
    @BindView(R.id.tv_ug_stats)
    TextView     tvUgStats;
    @BindView(R.id.tv_ex_stats)
    TextView     tvExStats;

    @BindInt(R.integer.advanced_deck_confirm_default_span_count)
    int intDefaultSpanCount;
    @BindInt(R.integer.recyclerview_default_margin)
    int intDefaultMargin;

    public static DeckManager mDeckManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deck_confirm;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(this::onBackPressed);
        viewAppBar.setTitleText(mDeckManager.getDeckName());
        // 初始化行容量
        int spanCount = SpUtils.getInt(SpConst.DeckConfirmSpanCount);
        if (-1 == spanCount) {
            spanCount = intDefaultSpanCount;
            SpUtils.putInt(SpConst.DeckConfirmSpanCount, intDefaultSpanCount);
        }
        // 初始化Ig区域
        DeckAdapter mIgDeckAdapter = new DeckAdapter(context, spanCount, intDefaultMargin);
        rvIg.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvIg.setAdapter(mIgDeckAdapter);
        // 初始化Ug区域
        DeckAdapter mUgDeckAdapter = new DeckAdapter(context, spanCount, intDefaultMargin);
        rvUg.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvUg.setAdapter(mUgDeckAdapter);
        // 初始化Ex区域
        DeckAdapter mEgDeckAdapter = new DeckAdapter(context, spanCount, intDefaultMargin);
        rvEx.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvEx.setAdapter(mEgDeckAdapter);
        // 填充界面
        mIgDeckAdapter.updateData(mDeckManager.getIgList());
        mUgDeckAdapter.updateData(mDeckManager.getUgList());
        mEgDeckAdapter.updateData(mDeckManager.getExList());
        tvIgStats.setText(String.format("%s / 20", mDeckManager.getIgList().size()));
        tvUgStats.setText(String.format("%s / 30", mDeckManager.getUgList().size()));
        tvExStats.setText(String.format("%s / 10", mDeckManager.getExList().size()));
    }
}
