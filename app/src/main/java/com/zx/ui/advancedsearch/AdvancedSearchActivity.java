package com.zx.ui.advancedsearch;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.zx.R;
import com.zx.bean.AbilityDetailBean;
import com.zx.bean.AbilityTypeBean;
import com.zx.bean.CardBean;
import com.zx.event.CardListEvent;
import com.zx.game.utils.CardUtils;
import com.zx.game.utils.RestrictUtils;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.deckeditor.DeckEditorActivity;
import com.zx.ui.main.MainActivity;
import com.zx.ui.result.ResultActivity;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.RxBus;
import com.zx.uitls.database.SQLiteUtils;
import com.zx.uitls.database.SqlUtils;
import com.zx.view.dialog.DialogCheckBox;
import com.zx.view.widget.AppBarView;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class AdvancedSearchActivity extends BaseActivity
{
    @BindView(R.id.view_content)
    RelativeLayout viewContent;
    @BindView(R.id.txt_key)
    EditText       txtKey;
    @BindView(R.id.txt_cost)
    EditText       txtCost;
    @BindView(R.id.txt_power)
    EditText       txtPower;
    @BindView(R.id.cmb_pack)
    Spinner        cmbPack;
    @BindView(R.id.cmb_illust)
    Spinner        cmbIllust;
    @BindView(R.id.cmb_type)
    Spinner        cmbType;
    @BindView(R.id.cmb_camp)
    Spinner        cmbCamp;
    @BindView(R.id.cmb_race)
    Spinner        cmbRace;
    @BindView(R.id.cmb_sign)
    Spinner        cmbSign;
    @BindView(R.id.cmb_rare)
    Spinner        cmbRare;
    @BindView(R.id.cmb_restrict)
    Spinner        cmbRestrict;
    @BindArray(R.array.camp)
    String         campArray[];
    @BindView(R.id.viewAppBar)
    AppBarView     viewAppBar;

    private String fromActivity;

    @Override
    public int getLayoutId() {
        return R.layout.activity_advanced_search;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        fromActivity = getIntent().getExtras().getString(Activity.class.getSimpleName(), "");
        cmbIllust.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CardUtils.getIllust()));
        cmbPack.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CardUtils.getAllPack()));
        cmbCamp.setOnItemSelectedListener(onCampItemSelectedListener);
        AbilityTypeBean.initAbilityTypeMap();
        AbilityDetailBean.initAbilityDetailMap();
    }

    @OnClick(R.id.btn_ability_type)
    public void onAbilityType_Click() {
        new DialogCheckBox(this, "基础分类", AbilityTypeBean.getAbilityTypeMap(), AbilityTypeBean::setAbilityTypeMap).show();
    }

    @OnClick(R.id.btn_ability_detail)
    public void onAbilityDetail_Click() {
        new DialogCheckBox(this, "扩展分类", AbilityDetailBean.getAbilityDetailMap(), AbilityDetailBean::setAbilityDetailMap).show();
    }

    @OnClick(R.id.fab_search)
    public void onSearch_Click() {
        CardBean       cardBean = getCardBean();
        String         querySql = SqlUtils.getQuerySql(cardBean);
        List<CardBean> cardList = SQLiteUtils.getCardList(querySql);
        cardList = RestrictUtils.getRestrictCardList(cardList, cardBean);
        if (cardList.size() == 0) {
            showToast("没有查询到相关卡牌");
        } else {
            if (fromActivity.equals(DeckEditorActivity.class.getSimpleName())) {
                RxBus.getInstance().post(new CardListEvent(cardList));
                super.onBackPressed();
            } else if (fromActivity.equals(MainActivity.class.getSimpleName())) {
                ResultActivity.cardBeanList = cardList;
                IntentUtils.gotoActivity(this, ResultActivity.class);
            }
        }
    }

    public CardBean getCardBean() {
        String Type          = cmbType.getSelectedItem().toString();
        String Camp          = cmbCamp.getSelectedItem().toString();
        String Race          = cmbRace.getSelectedItem().toString();
        String Sign          = cmbSign.getSelectedItem().toString();
        String Rare          = cmbRare.getSelectedItem().toString();
        String Pack          = cmbPack.getSelectedItem().toString();
        String Illust        = cmbIllust.getSelectedItem().toString();
        String Restrict      = cmbRestrict.getSelectedItem().toString();
        String Key           = txtKey.getText().toString().trim();
        String Cost          = txtCost.getText().toString().trim();
        String Power         = txtPower.getText().toString().trim();
        String AbilityType   = SqlUtils.getAbilityTypeSql(AbilityTypeBean.getAbilityTypeMap());
        String AbilityDetail = SqlUtils.getAbilityDetailSql(AbilityDetailBean.getAbilityDetailMap());
        return new CardBean(Key, Type, Camp, Race, Sign, Rare, Pack, Illust, Restrict, Cost, Power, AbilityType, AbilityDetail);
    }

    private AdapterView.OnItemSelectedListener onCampItemSelectedListener = new AdapterView.OnItemSelectedListener()
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cmbRace.setAdapter(new ArrayAdapter<>(AdvancedSearchActivity.this, android.R.layout.simple_spinner_item, CardUtils.getPartRace(campArray[position])));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @OnClick(R.id.fab_reset)
    public void onReset_Click() {
        cmbType.setSelection(0);
        cmbCamp.setSelection(0);
        cmbRace.setSelection(0);
        cmbSign.setSelection(0);
        cmbRare.setSelection(0);
        cmbPack.setSelection(0);
        cmbIllust.setSelection(0);
        cmbRestrict.setSelection(0);
        txtKey.setText("");
        txtCost.setText("");
        txtPower.setText("");
        AbilityTypeBean.initAbilityTypeMap();
        AbilityDetailBean.initAbilityDetailMap();
    }
}
