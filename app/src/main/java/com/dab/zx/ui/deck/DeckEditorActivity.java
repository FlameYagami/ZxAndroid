package com.dab.zx.ui.deck;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.dab.zx.bean.CardBean;
import com.dab.zx.bean.DeckBean;
import com.dab.zx.bean.DeckExBean;
import com.dab.zx.bean.DeckPreviewBean;
import com.dab.zx.config.Enum;
import com.dab.zx.config.SpConst;
import com.dab.zx.event.CardListEvent;
import com.dab.zx.game.DeckManager;
import com.dab.zx.game.utils.CardUtils;
import com.dab.zx.game.utils.DeckUtils;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.ui.search.AdvancedSearchActivity;
import com.dab.zx.ui.search.CardDetailActivity;
import com.dab.zx.uitls.BundleUtils;
import com.dab.zx.uitls.DisplayUtils;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.SpUtils;
import com.dab.zx.uitls.database.SQLiteUtils;
import com.dab.zx.uitls.database.SqlUtils;
import com.dab.zx.view.banner.BannerImageLoader;
import com.dab.zx.view.banner.BannerPageChangeListener;
import com.dab.zx.view.charts.model.Axis;
import com.dab.zx.view.charts.model.AxisValue;
import com.dab.zx.view.charts.model.Column;
import com.dab.zx.view.charts.model.ColumnChartData;
import com.dab.zx.view.charts.model.SubcolumnValue;
import com.dab.zx.view.charts.util.ChartUtils;
import com.dab.zx.view.charts.view.ColumnChartView;
import com.dab.zx.view.dialog.DialogConfirm;
import com.dab.zx.view.widget.AppBarView;
import com.michaelflisar.rxbus2.RxBusBuilder;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckEditorActivity extends BaseActivity {
    @BindView(R.id.rv_preview)
    RecyclerView    rvPreview;
    @BindView(R.id.img_pl)
    ImageView       imgPl;
    @BindView(R.id.rv_ig)
    RecyclerView    rvIg;
    @BindView(R.id.rv_ug)
    RecyclerView    rvUg;
    @BindView(R.id.rv_ex)
    RecyclerView    rvEx;
    @BindView(R.id.tv_cname)
    TextView        tvCname;
    @BindView(R.id.tv_number)
    TextView        tvNumber;
    @BindView(R.id.tv_race)
    TextView        tvRace;
    @BindView(R.id.tv_power)
    TextView        tvPower;
    @BindView(R.id.tv_cost)
    TextView        tvCost;
    @BindView(R.id.tv_ability)
    TextView        tvAbility;
    @BindView(R.id.banner)
    Banner          banner;
    @BindView(R.id.txt_search)
    EditText        txtSearch;
    @BindView(R.id.tv_result_count)
    TextView        tvResultCount;
    @BindView(R.id.tv_start_count)
    TextView        tvStartStats;
    @BindView(R.id.tv_life_count)
    TextView        tvLifeStats;
    @BindView(R.id.tv_void_count)
    TextView        tvVoidStats;
    @BindView(R.id.tv_ig_stats)
    TextView        tvIgStats;
    @BindView(R.id.tv_ug_stats)
    TextView        tvUgStats;
    @BindView(R.id.tv_ex_stats)
    TextView        tvExStats;
    @BindView(R.id.viewAppBar)
    AppBarView      viewAppBar;
    @BindView(R.id.chart)
    ColumnChartView charts;
    @BindView(R.id.viewContent)
    View            viewContent;

    @BindInt(R.integer.advanced_deck_editor_default_span_count)
    int intDefaultSpanCount;
    @BindInt(R.integer.recyclerview_default_margin)
    int intDefaultMargin;

    private static final String TAG = DeckEditorActivity.class.getSimpleName();

    private static final float imgScale = (float)7 / 5;

    private boolean isSaved = true;

    private CardAdapter              mCardPreAdapter;
    private DeckExAdapter            mDeckIgAdapter;
    private DeckExAdapter            mDeckUgAdapter;
    private DeckExAdapter            mDeckExAdapter;
    private DeckManager              mDeckManager;
    private BannerPageChangeListener bannerPageChangeListener;

    public static DeckPreviewBean deckPreviewBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deck_editor;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(this::onBackPressed);
        // 初始化行容量
        int spanCount = SpUtils.getInt(SpConst.DeckEditorSpanCount);
        if (-1 == spanCount) {
            spanCount = intDefaultSpanCount;
            SpUtils.putInt(SpConst.DeckEditorSpanCount, intDefaultSpanCount);
        }
        mCardPreAdapter = new CardAdapter(this, spanCount, intDefaultMargin);
        mDeckIgAdapter = new DeckExAdapter(this, spanCount, intDefaultMargin);
        mDeckUgAdapter = new DeckExAdapter(this, spanCount, intDefaultMargin);
        mDeckExAdapter = new DeckExAdapter(this, spanCount, intDefaultMargin);
        bannerPageChangeListener = new BannerPageChangeListener();
        // 设置RecyclerView最小高度
        int minWidthPx  = (DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(intDefaultMargin * 2)) / spanCount;
        int minHeightPx = (int)(minWidthPx * imgScale);
        // 初始化玩家容器长宽
        imgPl.setLayoutParams(new LinearLayout.LayoutParams(minWidthPx, minHeightPx));
        // 初始化统计控件长宽
        charts.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minHeightPx));
        // 初始化图片选择器
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setImageLoader(new BannerImageLoader());
        banner.setOnPageChangeListener(bannerPageChangeListener);
        // 初始化Ig区域
        rvIg.setMinimumHeight(minHeightPx);
        rvIg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvIg.setAdapter(mDeckIgAdapter);
        mDeckIgAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mDeckIgAdapter.setOnItemLongClickListener(this::removeCard);
        // 初始化Ug区域
        rvUg.setMinimumHeight(minHeightPx);
        rvUg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvUg.setAdapter(mDeckUgAdapter);
        mDeckUgAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mDeckUgAdapter.setOnItemLongClickListener(this::removeCard);
        // 初始化Ex区域
        rvEx.setMinimumHeight(minHeightPx);
        rvEx.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvEx.setAdapter(mDeckExAdapter);
        mDeckExAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mDeckExAdapter.setOnItemLongClickListener(this::removeCard);
        // 初始化查询预览区域
        rvPreview.setMinimumHeight(minHeightPx);
        rvPreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPreview.setAdapter(mCardPreAdapter);
        mCardPreAdapter.setOnItemClickListener(this::updateDetailByCard);
        mCardPreAdapter.setOnItemLongClickListener(this::addCard);
        // 初始化卡组管理类
        mDeckManager = new DeckManager(deckPreviewBean.getDeckName(), deckPreviewBean.getNumberExList());
        // 载入卡组
        updateAllRecyclerView();
        // 统计类型
        updateStats();
        // 注册高级查询事件监听
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(CardListEvent.class).subscribe(this::updatePreview));
    }

    @Override
    public void onBackPressed() {
        if (isSaved) {
            super.onBackPressed();
        } else if (DialogConfirm.show(this, getString(R.string.exit_with_not_saved))) {
            super.onBackPressed();
        }
    }

    /**
     * 高级查询
     */
    @OnClick(R.id.btn_adv_search)
    public void onAdvSearch_Click() {
        IntentUtils.gotoActivity(DeckEditorActivity.this, AdvancedSearchActivity.class,
                BundleUtils.putString(Activity.class.getSimpleName(), DeckEditorActivity.class.getSimpleName()));
    }

    /**
     * 卡组查看事件
     */
    @OnClick(R.id.btn_deck_detail)
    public void DeckDetail_Click() {
        DeckConfirmActivity.mDeckManager = mDeckManager;
        IntentUtils.gotoActivity(DeckEditorActivity.this, DeckConfirmActivity.class);
    }

    /**
     * 玩家卡查看事件
     */
    @OnClick(R.id.img_pl)
    public void onPlayer_Click() {
        if (mDeckManager.getPlayerList().size() != 0) {
            DeckBean deckBean = mDeckManager.getPlayerList().get(0);
            CardBean cardBean = CardUtils.getCardBean(deckBean.getNumberEx());
            updateDetail(cardBean);
        }
    }

    /**
     * 玩家卡删除事件
     */
    @OnLongClick(R.id.img_pl)
    public boolean onPlayer_LongClick() {
        if (mDeckManager.getPlayerList().size() != 0) {
            DeckBean      deckBean       = mDeckManager.getPlayerList().get(0);
            CardBean      cardBean       = CardUtils.getCardBean(deckBean.getNumberEx());
            Enum.AreaType areaType       = CardUtils.getAreaType(cardBean);
            Enum.AreaType returnAreaType = mDeckManager.deleteCard(areaType, deckBean.getNumberEx());
            updateSingleRecyclerView(returnAreaType, Enum.OperateType.Remove);
            isSaved = false;
        }
        return false;
    }

    /**
     * 更新查询列表
     */
    private void updatePreview(CardListEvent cardListEvent) {
        tvResultCount.setText(String.format("%s", cardListEvent.getCardList().size()));
        mCardPreAdapter.updateData(cardListEvent.getCardList());
    }

    /**
     * 更新详细信息(IG、UG、EX)
     */
    public void updateDetailByDeck(View view, List<?> data, int position) {
        DeckExBean deckEx = (DeckExBean)data.get(position);
        CardBean   card   = CardUtils.getCardBean(deckEx.getDeckBean().getNumberEx());
        updateDetail(card);
    }

    /**
     * 更新详细信息
     */
    public void updateDetailByCard(View view, List<?> data, int position) {
        CardBean cardBean = (CardBean)data.get(position);
        updateDetail(cardBean);
    }

    /**
     * 更新卡牌信息
     *
     * @param cardBean 卡牌模型
     */
    private void updateDetail(CardBean cardBean) {
        tvCname.setText(cardBean.getCName());
        tvNumber.setText(cardBean.getNumber());
        tvPower.setText(cardBean.getPower());
        tvCost.setText(cardBean.getCost());
        tvRace.setText(cardBean.getRace());
        tvAbility.setText(cardBean.getAbility());
        banner.setImages(CardUtils.getImagePathList(cardBean.getImage()));
        banner.start();
    }

    private void addCard(View view, List<?> data, int position) {
        CardBean      cardBean       = (CardBean)data.get(position);
        int           selectIndex    = bannerPageChangeListener.getCurrentIndex();
        String        numberEx       = CardUtils.getNumberExList(cardBean.getImage()).get(selectIndex);
        String        imagePath      = CardUtils.getImagePathList(cardBean.getImage()).get(selectIndex);
        Enum.AreaType areaType       = CardUtils.getAreaType(cardBean);
        Enum.AreaType returnAreaType = mDeckManager.addCard(areaType, numberEx, imagePath);
        updateSingleRecyclerView(returnAreaType, Enum.OperateType.Add);
        updateStats();
        // 标记修改
        isSaved = false;
    }

    public void removeCard(View view, List<?> data, int position) {
        DeckExBean    deckEx         = (DeckExBean)data.get(position);
        String        numberEx       = deckEx.getDeckBean().getNumberEx();
        Enum.AreaType areaType       = CardUtils.getAreaType(numberEx);
        Enum.AreaType returnAreaType = mDeckManager.deleteCard(areaType, numberEx);
        updateSingleRecyclerView(returnAreaType, Enum.OperateType.Remove);
        updateStats();
        // 标记修改
        isSaved = false;
    }

    @OnClick(R.id.fab_search)
    public void onSearch_Click() {
        DisplayUtils.hideKeyboard(this);
        String         querySql = SqlUtils.getKeyQuerySql(txtSearch.getText().toString().trim());
        List<CardBean> cardList = SQLiteUtils.getCardList(querySql);
        tvResultCount.setText(String.format("%s", cardList.size()));
        mCardPreAdapter.updateData(cardList);
        if (cardList.size() == 0) {
            showSnackBar(viewContent, getString(R.string.card_not_found));
        }
    }

    @OnLongClick(R.id.banner)
    public boolean onBannerPreview_LongClick() {
        String number = tvNumber.getText().toString();
        CardDetailActivity.cardBean = CardUtils.getCardBean(number);
        IntentUtils.gotoActivity(this, CardDetailActivity.class);
        return false;
    }

    @OnClick(R.id.btn_deck_save)
    public void onDeckSave_Click() {
        isSaved = DeckUtils.saveDeck(mDeckManager);
        showSnackBar(viewContent, isSaved ? getString(R.string.save_succeed) : getString(R.string.save_failed));
    }

    private void updateSingleRecyclerView(Enum.AreaType areaType, Enum.OperateType operateType) {
        if (Enum.AreaType.None.equals(areaType)) {
            return;
        }
        // 添加成功则更新该区域
        if (operateType.equals(Enum.OperateType.Add)) {
            if (areaType.equals(Enum.AreaType.Player)) {
                updatePlayerView(mDeckManager.getPlayerList());
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mDeckIgAdapter.updateData(mDeckManager.getIgExList());
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mDeckUgAdapter.updateData(mDeckManager.getUgExList());
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mDeckExAdapter.updateData(mDeckManager.getExExList());
            }
        } else {
            if (areaType.equals(Enum.AreaType.Player)) {
                updatePlayerView(mDeckManager.getPlayerList());
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mDeckIgAdapter.updateData(mDeckManager.getIgExList());
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mDeckUgAdapter.updateData(mDeckManager.getUgExList());
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mDeckExAdapter.updateData(mDeckManager.getExExList());
            }
        }
    }

    /**
     * 更新玩家卡牌界面
     *
     * @param playerList 玩家数据集合
     */
    private void updatePlayerView(List<DeckBean> playerList) {
        if (0 != playerList.size()) {
            Glide.with(this).load(CardUtils.getPlayerPath(playerList.get(0).getNumberEx()))
                    .error(R.drawable.ic_unknown_thumbnail).into(imgPl);
        } else {

            Glide.with(this).load(getString(R.string.empty))
                    .error(null).into(imgPl);
        }
    }

    /**
     * 一次性更新界面
     */
    private void updateAllRecyclerView() {
        // 对卡组进行排序
        mDeckManager.orderDeck();
        // 更新卡组信息
        mDeckIgAdapter.updateData(mDeckManager.getIgExList());
        mDeckUgAdapter.updateData(mDeckManager.getUgExList());
        mDeckExAdapter.updateData(mDeckManager.getExExList());
        updatePlayerView(mDeckManager.getPlayerList());
    }

    private void updateStats() {
        deckStats();
        List<Integer> countStartAndLifeAndVoid = mDeckManager.getStartAndLifeAndVoidCount();
        tvStartStats.setText(String.format("%s", countStartAndLifeAndVoid.get(0)));
        tvLifeStats.setText(String.format("%s", countStartAndLifeAndVoid.get(1)));
        tvVoidStats.setText(String.format("%s", countStartAndLifeAndVoid.get(2)));
        tvIgStats.setText(String.format("%s / 20", mDeckManager.getIgList().size()));
        tvUgStats.setText(String.format("%s / 30", mDeckManager.getUgList().size()));
        tvExStats.setText(String.format("%s / 10", mDeckManager.getExList().size()));
    }

    /**
     * 卡组费用统计
     */
    private void deckStats() {
        Map<Integer, Integer> statsMap = mDeckManager.getStatsMap();
        // 统计控件设置
        charts.setVisibility(0 == statsMap.size() ? View.GONE : View.VISIBLE);
        if (0 != statsMap.size()) {
            List<Column>         columns       = new ArrayList<>();
            List<AxisValue>      axisValueList = new ArrayList<>();
            List<SubcolumnValue> values;
            for (Map.Entry<Integer, Integer> entry : statsMap.entrySet()) {
                values = new ArrayList<>();
                values.add(new SubcolumnValue(entry.getValue(), ChartUtils.pickColor()));
                Column column = new Column(values);
                column.setHasLabels(true);
                columns.add(column);
                axisValueList.add(new AxisValue(entry.getKey()));
            }
            ColumnChartData data  = new ColumnChartData(columns);
            Axis            axisX = new Axis().setValues(axisValueList).setHasLines(true).setTextColor(R.color.black);
            data.setAxisXBottom(axisX);
            charts.setColumnChartData(data);
        }
    }
}
