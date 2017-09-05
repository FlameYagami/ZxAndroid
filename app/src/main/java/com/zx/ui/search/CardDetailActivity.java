package com.zx.ui.search;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.game.utils.CardUtils;
import com.zx.ui.base.BaseActivity;
import com.zx.view.banner.BannerImageLoader;
import com.zx.view.widget.AppBarView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/13.
 */

public class CardDetailActivity extends BaseActivity
{

    @BindView(R.id.banner)
    Banner      banner;
    @BindViews({R.id.img_camp0, R.id.img_camp1, R.id.img_camp2, R.id.img_camp3, R.id.img_camp4})
    ImageView[] imgCampList;
    @BindView(R.id.tv_number)
    TextView    tvNumber;
    @BindView(R.id.tv_rare)
    TextView    tvRare;
    @BindView(R.id.tv_race)
    TextView    tvRace;
    @BindView(R.id.tv_type)
    TextView    tvType;
    @BindView(R.id.tv_cname)
    TextView    tvCname;
    @BindView(R.id.tv_jname)
    TextView    tvJname;
    @BindView(R.id.tv_power)
    TextView    tvPower;
    @BindView(R.id.tv_cost)
    TextView    tvCost;
    @BindView(R.id.img_sign)
    ImageView   imgSign;
    @BindView(R.id.tv_ability)
    TextView    tvAbility;
    @BindView(R.id.tv_lines)
    TextView    tvLines;
    @BindView(R.id.viewAppBar)
    AppBarView  viewAppBar;

    public static CardBean cardBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_detail;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        setCardBean();
    }

    private void setCardBean() {
        tvNumber.setText(cardBean.getNumber());
        tvRace.setText(cardBean.getRace());
        tvType.setText(cardBean.getType());
        tvCname.setText(cardBean.getCName());
        tvJname.setText(cardBean.getJName());
        tvPower.setText(cardBean.getPower());
        tvCost.setText(cardBean.getCost());
        tvAbility.setText(cardBean.getAbility());
        tvLines.setText(cardBean.getLines());
        tvRare.setText(cardBean.getRare());
//        int rareResId = CardUtils.getRareResIdList(cardBean.getRare());
//        if (-1 == rareResId) {
//            imgSign.setImageBitmap(null);
//        } else {
//            Glide.with(this).load(rareResId).error(null).into(imgRare);
//        }

        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(CardUtils.getImagePathList(cardBean.getImage()));
        banner.start();

        List<Integer> campResIdList = CardUtils.getCampResIdList(cardBean.getCamp());
        int           signResId     = CardUtils.getSignResId(cardBean.getSign());
        if (-1 == signResId) {
            imgSign.setImageBitmap(null);
        } else {
            Glide.with(this).load(signResId).error(null).into(imgSign);
        }
        for (int i = 0; i != campResIdList.size(); i++) {
            if (-1 == campResIdList.get(i)) {
                imgCampList[i].setImageBitmap(null);
            } else {
                Glide.with(this).load(campResIdList.get(i)).error(null).into(imgCampList[i]);
            }
        }
    }
}
