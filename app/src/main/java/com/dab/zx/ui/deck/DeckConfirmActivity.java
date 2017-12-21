package com.dab.zx.ui.deck;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dab.zx.R;
import com.dab.zx.bean.DeckBean;
import com.dab.zx.config.SpConst;
import com.dab.zx.game.DeckManager;
import com.dab.zx.game.utils.CardUtils;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.ui.search.CardDetailActivity;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.SpUtils;
import com.dab.zx.view.widget.AppBarView;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dab.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/9/4.
 */

public class DeckConfirmActivity extends BaseActivity {
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
        mIgDeckAdapter.setOnItemClickListener(this::onItem_Click);
        // 初始化Ug区域
        DeckAdapter mUgDeckAdapter = new DeckAdapter(context, spanCount, intDefaultMargin);
        rvUg.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvUg.setAdapter(mUgDeckAdapter);
        mUgDeckAdapter.setOnItemClickListener(this::onItem_Click);
        // 初始化Ex区域
        DeckAdapter mExDeckAdapter = new DeckAdapter(context, spanCount, intDefaultMargin);
        rvEx.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvEx.setAdapter(mExDeckAdapter);
        mExDeckAdapter.setOnItemClickListener(this::onItem_Click);
        // 填充界面
        mIgDeckAdapter.updateData(mDeckManager.getIgList());
        mUgDeckAdapter.updateData(mDeckManager.getUgList());
        mExDeckAdapter.updateData(mDeckManager.getExList());
        tvIgStats.setText(String.format("%s / 20", mDeckManager.getIgList().size()));
        tvUgStats.setText(String.format("%s / 30", mDeckManager.getUgList().size()));
        tvExStats.setText(String.format("%s / 10", mDeckManager.getExList().size()));
    }

    private void onItem_Click(View view, List<?> objects, int i) {
        DeckBean deck = (DeckBean)objects.get(i);
        CardDetailActivity.cardBean = CardUtils.getCardBean(deck.getNumberEx());
        IntentUtils.gotoActivity(this, CardDetailActivity.class);
    }

}
