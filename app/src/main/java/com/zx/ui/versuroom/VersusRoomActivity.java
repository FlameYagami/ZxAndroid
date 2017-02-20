package com.zx.ui.versuroom;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.config.MyApp;
import com.zx.event.EnterRoomEvent;
import com.zx.event.LeaveRoomEvent;
import com.zx.game.message.ModBus;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.RxBus;
import com.zx.view.dialog.DialogDeckPreview;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class VersusRoomActivity extends BaseActivity
{
    @BindString(R.string.host)
    String host;
    @BindString(R.string.guest)
    String guest;

    @BindView(R.id.view_content)
    LinearLayout   viewContent;
    @BindView(R.id.tv_room_id)
    TextView       tvRoomId;
    @BindView(R.id.tv_name_player0)
    TextView       tvNamePlayer0;
    @BindView(R.id.tv_name_player1)
    TextView       tvNamePlayer1;
    @BindView(R.id.tv_player_type_0)
    TextView       tvPlayerType0;
    @BindView(R.id.tv_player_type_1)
    TextView       tvPlayerType1;
    @BindView(R.id.tv_deck_name_player0)
    TextView       tvDeckNamePlayer0;
    @BindView(R.id.tv_deck_name_player1)
    TextView       tvDeckNamePlayer1;
    @BindView(R.id.img_deck_player0)
    ImageView      imgDeckPlayer0;
    @BindView(R.id.img_deck_player1)
    ImageView      imgDeckPlayer1;
    @BindView(R.id.view_player0)
    RelativeLayout viewPlayer0;
    @BindView(R.id.view_player1)
    RelativeLayout viewPlayer1;
    @BindView(R.id.view_hint_player0)
    RelativeLayout viewHintPlayer0;
    @BindView(R.id.view_hint_player1)
    RelativeLayout viewHintPlayer1;

    public static EnterRoomEvent mEnterRoomEvent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_versus_room;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        tvRoomId.setText(String.valueOf(mEnterRoomEvent.getRoomId()));
        viewPlayer0.setVisibility(View.VISIBLE);
        viewHintPlayer0.setVisibility(View.GONE);
        tvNamePlayer0.setText(mEnterRoomEvent.getPlayerName());
        if (mEnterRoomEvent.getPlayerType() == 0) {
            tvPlayerType0.setText(host);
        } else if (mEnterRoomEvent.getPlayerType() == 1) {
            tvPlayerType0.setText(guest);
        } else {
            tvPlayerType0.setText(host);
            tvPlayerType1.setText(guest);
        }

        // 离开房间事件注册
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(LeaveRoomEvent.class).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onLeaveRoom));
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        showDialog("");
        MyApp.mMessageManager.sendMessage(ModBus.onLeaveRoom());
    }

    @Override
    public void onBackPressed() {
        showDialog("");
        MyApp.mMessageManager.sendMessage(ModBus.onLeaveRoom());
    }

    @OnClick(R.id.img_deck_player0)
    public void onDeckOwner_Click() {
        new DialogDeckPreview(this, (dialog, bean) -> {
            Glide.with(this).load(bean.getPlayerPath()).into(imgDeckPlayer0);
            tvDeckNamePlayer0.setText(bean.getDeckName());
        }).show();
    }

    /**
     * 离开房间事件
     */
    private void onLeaveRoom(LeaveRoomEvent mLeaveRoomEvent) {
        if (mLeaveRoomEvent.getPlayerType() == 0) {
            showSnackBar(viewContent, mLeaveRoomEvent.getPlayerName() + "撤销了房间");
        } else if (mLeaveRoomEvent.getPlayerType() == 1) {
            showSnackBar(viewContent, mLeaveRoomEvent.getPlayerName() + "离开了房间");
            viewPlayer1.setVisibility(View.GONE);
            viewHintPlayer1.setVisibility(View.VISIBLE);
        } else {
            viewPlayer1.setVisibility(View.GONE);
            viewHintPlayer1.setVisibility(View.VISIBLE);
            showSnackBar(viewContent, mLeaveRoomEvent.getPlayerName() + "离开了房间");
        }
        if (mEnterRoomEvent.getPlayerType() == mLeaveRoomEvent.getPlayerType()) {
            super.onBackPressed();
        }
    }
}
