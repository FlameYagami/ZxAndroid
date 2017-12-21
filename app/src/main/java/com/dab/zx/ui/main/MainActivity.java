package com.dab.zx.ui.main;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dab.zx.R;
import com.dab.zx.bean.CardBean;
import com.dab.zx.config.MapConst;
import com.dab.zx.game.utils.RestrictUtils;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.ui.deck.DeckPreviewActivity;
import com.dab.zx.ui.search.AdvancedSearchActivity;
import com.dab.zx.ui.search.CardPreviewActivity;
import com.dab.zx.ui.setting.SettingActivity;
import com.dab.zx.uitls.AppManager;
import com.dab.zx.uitls.BundleUtils;
import com.dab.zx.uitls.DisplayUtils;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.SystemUtils;
import com.dab.zx.uitls.database.SQLiteUtils;
import com.dab.zx.uitls.database.SqlUtils;
import com.dab.zx.view.banner.BannerImageLoader;
import com.dab.zx.view.widget.AppBarView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static br.com.zbra.androidlinq.Linq.stream;

public class MainActivity extends BaseActivity {
    @BindView(R.id.banner)
    Banner     bannerGuide;
    @BindView(R.id.txt_search)
    EditText   txtSearch;
    @BindView(R.id.viewAppBar)
    AppBarView viewAppBar;
    @BindView(R.id.viewContent)
    View       viewContent;

    @BindInt(R.integer.recyclerview_default_margin)
    int intRvDefaultMargin;

    private static final String TAG = MainActivity.class.getSimpleName();

    private long firstTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        // 安装位置判断
        if (SystemUtils.isInstallOnSDCard()) {
            showToast(getString(R.string.location_hint));
        }
        // 主界面不可调用SwipeBack
        setSwipeBackEnable(false);
        initBGABanner();
        Observable.just(this).observeOn(Schedulers.newThread()).subscribe(mainActivity -> {
            RestrictUtils.getRestrictList();
            SQLiteUtils.getAllCardList();
        });
    }

    /**
     * 初始化导航控件
     */
    private void initBGABanner() {
        float scale    = (float)29 / 68;
        int   heightPx = (int)((DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(intRvDefaultMargin * 2)) * scale);

        bannerGuide.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
        bannerGuide.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        bannerGuide.setIndicatorGravity(BannerConfig.RIGHT);
        bannerGuide.setImageLoader(new BannerImageLoader());
        bannerGuide.setImages(stream(MapConst.GuideMap.entrySet()).select(Map.Entry::getValue).toList());
        bannerGuide.setOnBannerClickListener(onBannerClick);
        bannerGuide.start();
    }

    /**
     * 导航点击事件
     */
    OnBannerClickListener onBannerClick = position -> {
        String querySql = SqlUtils.getPackQuerySql(stream(MapConst.GuideMap.entrySet()).select(Map.Entry::getKey).toList().get(position - 1));
        CardPreviewActivity.cardBeanList = SQLiteUtils.getCardList(querySql);
        IntentUtils.gotoActivity(MainActivity.this, CardPreviewActivity.class);
    };

    /**
     * 关键字搜索
     */
    @OnClick(R.id.fab_search)
    public void onSearch_Click() {
        DisplayUtils.hideKeyboard(this);
        String         querySql     = SqlUtils.getKeyQuerySql(txtSearch.getText().toString().trim());
        List<CardBean> cardBeanList = SQLiteUtils.getCardList(querySql);
        if (0 == cardBeanList.size()) {
            showSnackBar(viewContent, getString(R.string.card_not_found));
            return;
        }
        CardPreviewActivity.cardBeanList = cardBeanList;
        IntentUtils.gotoActivity(this, CardPreviewActivity.class);
    }

    /**
     * 回退事件
     */
    @Override
    public void onBackPressed() {
        long lastTime = System.currentTimeMillis();
        long between  = lastTime - firstTime;
        if (between > 2000) {
            firstTime = lastTime;
            showSnackBar(viewContent, getString(R.string.exit_with_more_back));
            return;
        }
        AppManager.AppExit(this);
    }

    /**
     * 高级搜索事件
     */
    @OnClick(R.id.btn_adv_search)
    public void onAdvSearch_Click() {
        IntentUtils.gotoActivity(this, AdvancedSearchActivity.class,
                BundleUtils.putString(Activity.class.getSimpleName(), MainActivity.class.getSimpleName()));
    }

    /**
     * 卡组预览事件
     */
    @OnClick(R.id.btn_deck_preview)
    public void onDeckPreview_Click() {
        IntentUtils.gotoActivity(this, DeckPreviewActivity.class);
    }

    /**
     * 设置事件
     */
    @OnClick(R.id.btn_setting)
    public void onSetting_Click() {
        IntentUtils.gotoActivity(this, SettingActivity.class);
    }
}
