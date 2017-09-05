package com.zx.ui.deck;

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
import com.michaelflisar.rxbus2.RxBusBuilder;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.DeckBean;
import com.zx.bean.DeckPreviewBean;
import com.zx.config.Enum;
import com.zx.config.SpConst;
import com.zx.event.CardListEvent;
import com.zx.game.DeckManager;
import com.zx.game.utils.CardUtils;
import com.zx.game.utils.DeckUtils;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.search.AdvancedSearchActivity;
import com.zx.ui.search.CardDetailActivity;
import com.zx.uitls.BundleUtils;
import com.zx.uitls.DisplayUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.PathManager;
import com.zx.uitls.SpUtils;
import com.zx.uitls.database.SQLiteUtils;
import com.zx.uitls.database.SqlUtils;
import com.zx.view.banner.BannerImageLoader;
import com.zx.view.banner.BannerPageChangeListener;
import com.zx.view.charts.model.Axis;
import com.zx.view.charts.model.AxisValue;
import com.zx.view.charts.model.Column;
import com.zx.view.charts.model.ColumnChartData;
import com.zx.view.charts.model.SubcolumnValue;
import com.zx.view.charts.util.ChartUtils;
import com.zx.view.charts.view.ColumnChartView;
import com.zx.view.dialog.DialogConfirm;
import com.zx.view.widget.AppBarView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckEditorActivity extends BaseActivity
{
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

    @BindString(R.string.save_succeed)
    String saveSucceed;
    @BindString(R.string.save_failed)
    String saveFailed;
    @BindInt(R.integer.advanced_deck_editor_default_span_count)
    int    mDefaultSpanCount;

    private static final String TAG = DeckEditorActivity.class.getSimpleName();

    private static final int   rvMargin = 16;
    private static final float imgScale = (float)7 / 5;

    private boolean isSaved = true;

    CardAdapter              mCardPreAdapter;
    DeckAdapter              mDeckIgAdapter;
    DeckAdapter              mDeckUgAdapter;
    DeckAdapter              mDeckExAdapter;
    DeckManager              mDeckManager;
    BannerPageChangeListener bannerPageChangeListener;

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
            spanCount = mDefaultSpanCount;
            SpUtils.putInt(SpConst.DeckEditorSpanCount, mDefaultSpanCount);
        }
        mCardPreAdapter = new CardAdapter(this, spanCount, rvMargin);
        mDeckIgAdapter = new DeckAdapter(this, spanCount, rvMargin);
        mDeckUgAdapter = new DeckAdapter(this, spanCount, rvMargin);
        mDeckExAdapter = new DeckAdapter(this, spanCount, rvMargin);
        bannerPageChangeListener = new BannerPageChangeListener();
        // 设置RecyclerView最小高度
        int minWidthPx  = (DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(rvMargin * 2)) / spanCount;
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
        // 初始话卡组管理类
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
        } else if (DialogConfirm.show(this, "卡组尚未保存,是否放弃本次编辑?")) {
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
            DeckBean      deckBean = mDeckManager.getPlayerList().get(0);
            CardBean      cardBean = CardUtils.getCardBean(deckBean.getNumberEx());
            Enum.AreaType areaType = CardUtils.getAreaType(cardBean);
            updateSingleRecyclerView(areaType, Enum.OperateType.Remove, -1);
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
     * 更新详细信息
     */
    public void updateDetailByDeck(View view, List<?> data, int position) {
        DeckBean deckBean = (DeckBean)data.get(position);
        CardBean cardBean = CardUtils.getCardBean(deckBean.getNumberEx());
        updateDetail(cardBean);
    }

    /**
     * 更新详细信息
     */
    public void updateDetailByCard(View view, List<?> data, int position) {
        CardBean cardBean = (CardBean)data.get(position);
        updateDetail(cardBean);
    }

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
        String        numberEx       = CardUtils.getNumberEx(cardBean.getImage(), bannerPageChangeListener.getCurrentIndex());
        String        imagePath      = PathManager.pictureCache + File.separator + numberEx + context.getString(R.string.image_extension);
        Enum.AreaType areaType       = CardUtils.getAreaType(cardBean);
        Enum.AreaType returnAreaType = mDeckManager.addCard(areaType, numberEx, imagePath);
        updateSingleRecyclerView(returnAreaType, Enum.OperateType.Add, position);
        updateStats();
        isSaved = false;
    }

    public void removeCard(View view, List<?> data, int position) {
        DeckBean      deckBean       = (DeckBean)data.get(position);
        String        numberEx       = deckBean.getNumberEx();
        Enum.AreaType areaType       = CardUtils.getAreaType(numberEx);
        Enum.AreaType returnAreaType = mDeckManager.deleteCard(areaType, numberEx);
        updateSingleRecyclerView(returnAreaType, Enum.OperateType.Remove, position);
        updateStats();
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
            showToast("没有查询到相关卡牌");
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
        showToast(isSaved ? saveSucceed : saveFailed);
    }

    private void updateSingleRecyclerView(Enum.AreaType areaType, Enum.OperateType operateType, int position) {
        if (Enum.AreaType.None.equals(areaType)) {
            return;
        }
        // 添加成功则更新该区域
        if (operateType.equals(Enum.OperateType.Add)) {
            if (areaType.equals(Enum.AreaType.Player)) {
                Glide.with(this).load(PathManager.pictureCache + File.separator + mDeckManager.getPlayerList().get(0).getNumberEx() + context.getString(R.string.image_extension))
                        .error(R.drawable.ic_unknown_picture).into(imgPl);
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mDeckIgAdapter.addData(mDeckManager.getIgList(), mDeckManager.getIgList().size() - 1);
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mDeckUgAdapter.addData(mDeckManager.getUgList(), mDeckManager.getUgList().size() - 1);
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mDeckExAdapter.addData(mDeckManager.getExList(), mDeckManager.getExList().size() - 1);
            }
        } else {
            if (areaType.equals(Enum.AreaType.Player)) {
                Glide.with(this).load(R.drawable.ic_unknown_thumbnail)
                        .error(R.drawable.ic_unknown_picture).into(imgPl);
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mDeckIgAdapter.removeData(mDeckManager.getIgList(), position);
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mDeckUgAdapter.removeData(mDeckManager.getUgList(), position);
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mDeckExAdapter.removeData(mDeckManager.getExList(), position);
            }
        }
    }

    private void updateAllRecyclerView() {
        if (0 != mDeckManager.getPlayerList().size()) {
            Glide.with(this).load(PathManager.pictureCache + File.separator + mDeckManager.getPlayerList().get(0).getNumberEx() + context.getString(R.string.image_extension)).error(null).into(imgPl);
        }
        mDeckManager.orderDeck();
        mDeckIgAdapter.updateData(mDeckManager.getIgList());
        mDeckUgAdapter.updateData(mDeckManager.getUgList());
        mDeckExAdapter.updateData(mDeckManager.getExList());
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
        // 统计集合设置
        Map<Integer, Integer> statsMap     = new LinkedHashMap<>();
        List<Integer>         costIgList   = stream(mDeckManager.getIgList()).select(DeckBean::getCost).toList();
        List<Integer>         costUgList   = stream(mDeckManager.getUgList()).select(DeckBean::getCost).toList();
        List<Integer>         costDeckList = new LinkedList<>();
        costDeckList.addAll(costIgList);
        costDeckList.addAll(costUgList);
        if (0 != costDeckList.size()) {
            int costMax = Collections.max(costDeckList);
            for (int i = 0; i != costMax; i++) {
                int finalI = i;
                statsMap.put(i, stream(costDeckList).where(cost -> cost.equals(finalI + 1)).count());
            }
        }

        // 统计控件设置
        if (0 != costDeckList.size()) {
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
        charts.setVisibility(0 == costDeckList.size() ? View.GONE : View.VISIBLE);
    }
}
