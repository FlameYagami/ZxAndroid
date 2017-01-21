package com.zx.ui.gamesetting;

import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zx.R;
import com.zx.bean.PlayerBean;
import com.zx.config.SpConst;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.duel.DuelActivity;
import com.zx.uitls.DeckUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class GameSettingActivity extends BaseActivity
{
    @BindView(R.id.view_content)
    RelativeLayout    viewContent;
    @BindView(R.id.cmb_deck_select)
    Spinner           cmbDeckSelect;
    @BindView(R.id.tv_nickname0)
    TextView          tvNickname0;
    @BindView(R.id.chk_player0)
    AppCompatCheckBox chkPlayer0;
    @BindView(R.id.tv_nickname1)
    TextView          tvNickname1;
    @BindView(R.id.chk_player1)
    AppCompatCheckBox chkPlayer1;

    private boolean isHost;
    private List<PlayerBean> mPlayerList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_game_setting;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        isHost = getIntent().getExtras().getBoolean(PlayerBean.class.getSimpleName(), false);
        mPlayerList.add(new PlayerBean(SpUtil.getInstances().getString(SpConst.NickName), isHost));
        cmbDeckSelect.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DeckUtils.getDeckNameList()));
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_duel_start)
    public void onDuelStart() {
        Observable.just(this).subscribe(activity -> {
            if (2 != stream(mPlayerList).where(PlayerBean::isPrepare).count()) {
                return;
            }
            IntentUtils.gotoActivity(activity, DuelActivity.class);
        });
    }

    @OnClick({R.id.chk_player0, R.id.chk_player1})
    public void onPlayer_Click() {
        Observable.just(cmbDeckSelect.getSelectedItem().toString()).subscribe(deckName -> {
            if (TextUtils.isEmpty(deckName)) {
                showSnackBar(viewContent, "请选择卡组");
                return;
            }
            if (!DeckUtils.checkDeck(DeckUtils.getNumberListEx(deckName))) {
                showSnackBar(viewContent, "卡组不符合标准");
                return;
            }
            for (PlayerBean mPlayerBean : mPlayerList) {
                if (isHost && mPlayerBean.isHost()) {
                    mPlayerBean.setPrepare(!mPlayerBean.isPrepare());
                }
                if (!isHost && !mPlayerBean.isHost()) {
                    mPlayerBean.setPrepare(!mPlayerBean.isPrepare());
                }
            }
            // 发送消息告知
        });
    }

    private void updateUI() {
        for (PlayerBean mPlayerBean : mPlayerList) {
            if (mPlayerBean.isHost()) {
                tvNickname0.setText(mPlayerBean.getNickname());
                chkPlayer0.setChecked(mPlayerBean.isPrepare());
            } else {
                tvNickname1.setText(mPlayerBean.getNickname());
                chkPlayer1.setChecked(mPlayerBean.isPrepare());
            }
        }
    }
}
