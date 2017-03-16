package com.zx.ui.deckeditor;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.DeckBean;
import com.zx.bean.DeckPreviewBean;
import com.zx.config.Enum;
import com.zx.event.CardListEvent;
import com.zx.game.DeckManager;
import com.zx.game.utils.CardUtils;
import com.zx.game.utils.DeckUtils;
import com.zx.ui.advancedsearch.AdvancedSearchActivity;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.detail.DetailActivity;
import com.zx.uitls.BundleUtils;
import com.zx.uitls.DisplayUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.PathManager;
import com.zx.uitls.RxBus;
import com.zx.uitls.database.SQLiteUtils;
import com.zx.uitls.database.SqlUtils;
import com.zx.view.banner.BannerImageLoader;
import com.zx.view.banner.BannerPageChangeListener;

import java.io.File;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckEditorActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener
{
    @BindView(R.id.rv_preview)
    RecyclerView rvPreview;
    @BindView(R.id.img_pl)
    ImageView    imgPl;
    @BindView(R.id.rv_ig)
    RecyclerView rvIg;
    @BindView(R.id.rv_ug)
    RecyclerView rvUg;
    @BindView(R.id.rv_ex)
    RecyclerView rvEx;
    @BindView(R.id.tv_cname)
    TextView     tvCname;
    @BindView(R.id.tv_number)
    TextView     tvNumber;
    @BindView(R.id.tv_race)
    TextView     tvRace;
    @BindView(R.id.tv_power)
    TextView     tvPower;
    @BindView(R.id.tv_cost)
    TextView     tvCost;
    @BindView(R.id.tv_ability)
    TextView     tvAbility;
    @BindView(R.id.banner)
    Banner       banner;
    @BindView(R.id.txt_search)
    EditText     txtSearch;
    @BindView(R.id.view_content)
    LinearLayout viewContent;
    @BindString(R.string.save_succeed)
    String       saveSucceed;
    @BindString(R.string.save_failed)
    String       saveFailed;
    @BindView(R.id.img_menu)
    ImageView    viewMenu;
    @BindView(R.id.tv_result_count)
    TextView     tvResultCount;
    @BindView(R.id.tv_start_count)
    TextView     tvStartCount;
    @BindView(R.id.tv_life_count)
    TextView     tvLifeCount;
    @BindView(R.id.tv_void_count)
    TextView     tvVoidCount;

    private static final String TAG = DeckEditorActivity.class.getSimpleName();

    CardAdapter              mPreviewCardAdapter      = new CardAdapter(this);
    DeckAdapter              mIgDeckAdapter           = new DeckAdapter(this);
    DeckAdapter              mUgDeckAdapter           = new DeckAdapter(this);
    DeckAdapter              mEgDeckAdapter           = new DeckAdapter(this);
    BannerPageChangeListener bannerPageChangeListener = new BannerPageChangeListener();
    DeckManager mDeckManager;

    public static DeckPreviewBean deckPreviewBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deck_editor;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        viewMenu.setOnClickListener(this::showPopMenu);

        int minHeightPx = (DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(15)) / 10 * 7 / 5;
        rvIg.setMinimumHeight(minHeightPx);
        rvUg.setMinimumHeight(minHeightPx);
        rvEx.setMinimumHeight(minHeightPx);
        rvPreview.setMinimumHeight(minHeightPx);

        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setImageLoader(new BannerImageLoader());
        banner.setOnPageChangeListener(bannerPageChangeListener);

        rvIg.setLayoutManager(new GridLayoutManager(this, 10));
        rvIg.setAdapter(mIgDeckAdapter);
        mIgDeckAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mIgDeckAdapter.setOnItemLongClickListener(this::removeCard);

        rvUg.setLayoutManager(new GridLayoutManager(this, 10));
        rvUg.setAdapter(mUgDeckAdapter);
        mUgDeckAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mUgDeckAdapter.setOnItemLongClickListener(this::removeCard);

        rvEx.setLayoutManager(new GridLayoutManager(this, 10));
        rvEx.setAdapter(mEgDeckAdapter);
        mEgDeckAdapter.setOnItemClickListener(this::updateDetailByDeck);
        mEgDeckAdapter.setOnItemLongClickListener(this::removeCard);

        rvPreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPreview.setAdapter(mPreviewCardAdapter);
        mPreviewCardAdapter.setOnItemClickListener(this::updateDetailByCard);
        mPreviewCardAdapter.setOnItemLongClickListener(this::addCard);

        mDeckManager = new DeckManager(deckPreviewBean.getDeckName(), deckPreviewBean.getNumberExList());
        updateAllRecyclerView();
        updateStartAndLifeAndVoid(DeckUtils.getStartAndLifeAndVoidCount(mDeckManager));

        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CardListEvent.class).subscribe(this::updatePreview));
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }

    @OnClick(R.id.img_pl)
    public void onPlayer_Click() {
        if (mDeckManager.getPlayerList().size() != 0) {
            DeckBean deckBean = mDeckManager.getPlayerList().get(0);
            CardBean cardBean = CardUtils.getCardBean(deckBean.getNumberEx());
            updateDetail(cardBean);
        }
    }

    @OnLongClick(R.id.img_pl)
    public boolean onPlayer_LongClick() {
        if (mDeckManager.getPlayerList().size() != 0) {
            DeckBean      deckBean = mDeckManager.getPlayerList().get(0);
            CardBean      cardBean = CardUtils.getCardBean(deckBean.getNumberEx());
            Enum.AreaType areaType = CardUtils.getAreaType(cardBean);
            updateSingleRecyclerView(areaType, Enum.OperateType.Remove, -1);
        }
        return false;
    }

    /**
     * 更新查询列表
     */
    private void updatePreview(CardListEvent cardListEvent) {
        tvResultCount.setText(String.format("%s", cardListEvent.getCardList().size()));
        mPreviewCardAdapter.updateData(cardListEvent.getCardList());
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
        updateStartAndLifeAndVoid(DeckUtils.getStartAndLifeAndVoidCount(mDeckManager));
    }

    public void removeCard(View view, List<?> data, int position) {
        DeckBean      deckBean       = (DeckBean)data.get(position);
        String        numberEx       = deckBean.getNumberEx();
        Enum.AreaType areaType       = CardUtils.getAreaType(numberEx);
        Enum.AreaType returnAreaType = mDeckManager.deleteCard(areaType, numberEx);
        updateSingleRecyclerView(returnAreaType, Enum.OperateType.Remove, position);
        updateStartAndLifeAndVoid(DeckUtils.getStartAndLifeAndVoidCount(mDeckManager));
    }

    @OnClick(R.id.fab_search)
    public void onSearch_Click() {
        DisplayUtils.hideKeyboard(this);
        String         querySql     = SqlUtils.getKeyQuerySql(txtSearch.getText().toString().trim());
        List<CardBean> cardBeanList = SQLiteUtils.getCardList(querySql);
        tvResultCount.setText(String.format("%s", cardBeanList.size()));
        mPreviewCardAdapter.updateData(cardBeanList);
        if (cardBeanList.size() == 0) {
            showSnackBar(viewContent, "没有查询到相关卡牌");
        }
    }

    @OnLongClick(R.id.banner)
    public boolean onBannerPreview_LongClick() {
        String number = tvNumber.getText().toString();
        DetailActivity.cardBean = CardUtils.getCardBean(number);
        IntentUtils.gotoActivity(this, DetailActivity.class);
        return false;
    }

    @OnClick(R.id.tv_advanced_search)
    public void onAdvancedSearch_Click() {
        IntentUtils.gotoActivity(this, AdvancedSearchActivity.class, BundleUtils.putString(Activity.class.getSimpleName(), DeckEditorActivity.class.getSimpleName()));
    }

    @OnClick(R.id.tv_deck_save)
    public void onDeckSave_Click() {
        showSnackBar(viewContent, DeckUtils.saveDeck(mDeckManager) ? saveSucceed : saveFailed);
    }

    private void updateSingleRecyclerView(Enum.AreaType areaType, Enum.OperateType operateType, int position) {
        if (Enum.AreaType.None.equals(areaType)) {
            return;
        }
        // 添加成功则更新该区域
        if (operateType.equals(Enum.OperateType.Add)) {
            if (areaType.equals(Enum.AreaType.Player)) {
                Glide.with(this).load(PathManager.pictureCache + File.separator + mDeckManager.getPlayerList().get(0).getNumberEx() + context.getString(R.string.image_extension))
                        .error(R.drawable.img_unknown_picture).into(imgPl);
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mIgDeckAdapter.addData(mDeckManager.getIgList(), mDeckManager.getIgList().size() - 1);
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mUgDeckAdapter.addData(mDeckManager.getUgList(), mDeckManager.getUgList().size() - 1);
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mEgDeckAdapter.addData(mDeckManager.getExList(), mDeckManager.getExList().size() - 1);
            }
        } else {
            if (areaType.equals(Enum.AreaType.Player)) {
                imgPl.setImageBitmap(null);
            } else if (areaType.equals(Enum.AreaType.Ig)) {
                mIgDeckAdapter.removeData(mDeckManager.getIgList(), position);
            } else if (areaType.equals(Enum.AreaType.Ug)) {
                mUgDeckAdapter.removeData(mDeckManager.getUgList(), position);
            } else if (areaType.equals(Enum.AreaType.Ex)) {
                mEgDeckAdapter.removeData(mDeckManager.getExList(), position);
            }
        }
    }

    private void updateAllRecyclerView() {
        if (0 != mDeckManager.getPlayerList().size()) {
            Glide.with(this).load(PathManager.pictureCache + File.separator + mDeckManager.getPlayerList().get(0).getNumberEx() + context.getString(R.string.image_extension)).error(null).into(imgPl);
        }
        mIgDeckAdapter.updateData(mDeckManager.getIgList());
        mUgDeckAdapter.updateData(mDeckManager.getUgList());
        mEgDeckAdapter.updateData(mDeckManager.getExList());
    }

    private void updateStartAndLifeAndVoid(List<Integer> countStartAndLifeAndVoid) {
        int startCount = countStartAndLifeAndVoid.get(0);
        int lifeCount  = countStartAndLifeAndVoid.get(1);
        int voidCount  = countStartAndLifeAndVoid.get(2);
        tvStartCount.setText(String.format("%s", startCount));
        tvLifeCount.setText(String.format("%s", lifeCount));
        tvVoidCount.setText(String.format("%s", voidCount));
//        tvStartCount.setTextColor(startCount == 0 ? Color.RED : startCount == 1 ? Color.GREEN : Color.YELLOW);
//        tvLifeCount.setTextColor((lifeCount == 0) || (lifeCount == 1) ? Color.RED : lifeCount == 2 ? Color.YELLOW : Color.GREEN);
//        tvVoidCount.setTextColor((voidCount == 0) || (voidCount == 1) ? Color.RED : voidCount == 2 ? Color.YELLOW : Color.GREEN);
    }

    public void showPopMenu(View v) {
        PopupMenu    popup    = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_deck_editor_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order_by_value:
                mDeckManager.orderDeck(Enum.DeckOrderType.Value);
                updateAllRecyclerView();
                break;
            case R.id.nav_order_by_random:
                mDeckManager.orderDeck(Enum.DeckOrderType.Random);
                updateAllRecyclerView();
                break;
        }
        return false;
    }
}
