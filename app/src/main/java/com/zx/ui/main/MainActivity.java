package com.zx.ui.main;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.config.MapConst;
import com.zx.game.utils.RestrictUtils;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.deck.DeckPreviewActivity;
import com.zx.ui.search.AdvancedSearchActivity;
import com.zx.ui.search.CardPreviewActivity;
import com.zx.ui.setting.SettingActivity;
import com.zx.uitls.AppManager;
import com.zx.uitls.BundleUtils;
import com.zx.uitls.DisplayUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.database.SQLiteUtils;
import com.zx.uitls.database.SqlUtils;
import com.zx.view.banner.BannerImageLoader;
import com.zx.view.widget.AppBarView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static br.com.zbra.androidlinq.Linq.stream;

public class MainActivity extends BaseActivity
{
    @BindView(R.id.banner)
    Banner            bannerGuide;
    @BindView(R.id.view_content)
    CoordinatorLayout viewContent;
    @BindView(R.id.txt_search)
    EditText          txtSearch;
    @BindView(R.id.viewAppBar)
    AppBarView        viewAppBar;

    private static final String TAG       = MainActivity.class.getSimpleName();
    private              long   firstTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        // 主界面不可调用SwipeBack
        setSwipeBackEnable(false);
        initBGABanner();
        Observable.just(this).observeOn(Schedulers.newThread()).subscribe(mainActivity -> {
            RestrictUtils.getRestrictList();
            SQLiteUtils.getAllCardList();
        });
    }

    private void initBGABanner() {
        int                      heightPx = (DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(16 * 2)) * 29 / 68;
        int                      marginPx = DisplayUtils.dip2px(16);
        FrameLayout.LayoutParams params   = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        params.setMargins(marginPx, marginPx, marginPx, marginPx);
        bannerGuide.setLayoutParams(params);
        bannerGuide.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        bannerGuide.setIndicatorGravity(BannerConfig.RIGHT);
        bannerGuide.setImageLoader(new BannerImageLoader());
        bannerGuide.setImages(stream(MapConst.GuideMap.entrySet()).select(Map.Entry::getValue).toList());
        bannerGuide.setOnBannerClickListener(position -> {
            String querySql = SqlUtils.getPackQuerySql(stream(MapConst.GuideMap.entrySet()).select(Map.Entry::getKey).toList().get(position - 1));
            CardPreviewActivity.cardBeanList = SQLiteUtils.getCardList(querySql);
            IntentUtils.gotoActivity(this, CardPreviewActivity.class);
        });
        bannerGuide.start();
    }

    /**
     * 关键字搜索
     */
    @OnClick(R.id.fab_search)
    public void onSearch_Click() {
        DisplayUtils.hideKeyboard(this);
        String         querySql     = SqlUtils.getKeyQuerySql(txtSearch.getText().toString().trim());
        List<CardBean> cardBeanList = SQLiteUtils.getCardList(querySql);
        if (cardBeanList.size() == 0) {
            showToast("没有查询到相关卡牌");
        } else {
            CardPreviewActivity.cardBeanList = cardBeanList;
            IntentUtils.gotoActivity(this, CardPreviewActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        long lastTime = System.currentTimeMillis();
        long between  = lastTime - firstTime;
        if (between < 2000) {
            AppManager.getInstances().AppExit(this);
        } else {
            firstTime = lastTime;
            showSnackBar(viewContent, "再按一次退出应用");
        }
    }

    @OnClick(R.id.btn_adv_search)
    public void onAdvSearch_Click() {
        IntentUtils.gotoActivity(this, AdvancedSearchActivity.class,
                BundleUtils.putString(Activity.class.getSimpleName(), MainActivity.class.getSimpleName()));
    }

    @OnClick(R.id.btn_deck_preview)
    public void onDeckPreview_Click() {
        IntentUtils.gotoActivity(this, DeckPreviewActivity.class);
    }

    @OnClick(R.id.btn_setting)
    public void onSetting_Click() {
        IntentUtils.gotoActivity(this, SettingActivity.class);
    }
}
