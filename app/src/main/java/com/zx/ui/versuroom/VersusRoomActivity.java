package com.zx.ui.versuroom;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.config.MyApp;
import com.zx.event.EnterRoomEvent;
import com.zx.event.LeaveRoomEvent;
import com.zx.game.Player;
import com.zx.game.enums.PlayerType;
import com.zx.game.message.ModBus;
import com.zx.ui.base.BaseExActivity;
import com.zx.uitls.RxBus;
import com.zx.view.dialog.DialogDeckPreview;

import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class VersusRoomActivity extends BaseExActivity
{
    @BindString(R.string.host)
    String host;
    @BindString(R.string.guest)
    String guest;
    @BindString(R.string.ready)
    String ready;
    @BindString(R.string.cancel_ready)
    String cancelready;

    @BindView(R.id.view_content)
    LinearLayout   viewContent;
    @BindView(R.id.tv_room_id)
    TextView       tvRoomId;
    @BindViews({R.id.btn_ready_player0, R.id.btn_ready_player1})
    Button         btnReady[];
    @BindViews({R.id.tv_name_player0, R.id.tv_name_player1})
    TextView       tvName[];
    @BindViews({R.id.tv_player_type_0, R.id.tv_player_type_1})
    TextView       tvType[];
    @BindViews({R.id.tv_deck_name_player0, R.id.tv_deck_name_player1})
    TextView       tvDeckName[];
    @BindViews({R.id.img_deck_player0, R.id.img_deck_player1})
    ImageView      imgDeck[];
    @BindViews({R.id.view_player0, R.id.view_player1})
    RelativeLayout viewPlayer[];
    @BindViews({R.id.view_hint_player0, R.id.view_hint_player1})
    RelativeLayout viewHintPlayer[];

    private UISubscriber mUISubscriber;
    private int          ownerType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_versus_room;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(EnterRoomEvent.class).subscribe(this::onEnterRoom));
        // 离开房间事件注册
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(LeaveRoomEvent.class).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onLeaveRoom));
    }

    @Override
    protected void onResume() {
        mUISubscriber = new UISubscriber();
        super.onResume();
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        showDialog("");
        MyApp.Client.send(ModBus.onLeaveRoom());
    }

    @Override
    public void onBackPressed() {
        showDialog("");
        MyApp.Client.send(ModBus.onLeaveRoom());
    }

    @OnClick(R.id.img_deck_player0)
    public void onDeckOwner_Click() {
        new DialogDeckPreview(this, (dialog, bean) -> {
            Glide.with(this).load(bean.getPlayerPath()).into(imgDeck[ownerType]);
            tvDeckName[ownerType].setText(bean.getDeckName());
        }).show();
    }

    @Override
    protected void onPause() {
        if (null != mUISubscriber) {
            mUISubscriber.releaseSubscriber();
        }
        super.onPause();
    }


    /**
     * 进入房间事件
     */
    private void onEnterRoom(EnterRoomEvent mEnterRoomEvent) {
        showSnackBar(viewContent, mEnterRoomEvent.getPlayer().getName() + "进入了房间");
    }

    /**
     * 离开房间事件
     */
    private void onLeaveRoom(LeaveRoomEvent mLeaveRoomEvent) {
        if (mLeaveRoomEvent.getPlayerType() == 0) {
            showSnackBar(viewContent, mLeaveRoomEvent.getPlayerName() + "撤销了房间");
        }
        if (mLeaveRoomEvent.getPlayerType() == 1) {
            showSnackBar(viewContent, mLeaveRoomEvent.getPlayerName() + "离开了房间");
            viewPlayer[1].setVisibility(View.GONE);
            viewHintPlayer[1].setVisibility(View.VISIBLE);
        }
        if (MyApp.Client.Player.getType() == mLeaveRoomEvent.getPlayerType()) {
            super.onBackPressed();
        }
    }

    /**
     * 更新界面
     */
    private void updateUI() {
        ownerType = MyApp.Client.Player.getType();
        tvRoomId.setText(MyApp.Client.Room.getRoomId());

        Player[] players = MyApp.Client.Room.getDuelists();
        for (int i = 0; i != 2; i++) {
            if (null == players[i]) {
                viewPlayer[i].setVisibility(View.GONE);
                viewHintPlayer[i].setVisibility(View.VISIBLE);
            } else {
                viewPlayer[i].setVisibility(View.VISIBLE);
                viewHintPlayer[i].setVisibility(View.GONE);
                tvType[i].setText(players[i].getType() == PlayerType.Host ? host : guest);
                tvName[i].setText(players[i].getName());
                btnReady[i].setText(players[i].isReady() ? cancelready : ready);
            }
        }
    }

    /**
     * 界面轮询
     */
    private class UISubscriber
    {
        Subscription stopMePlease;

        UISubscriber() {
            stopMePlease = Observable.interval(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                updateUI();
            });
        }

        void releaseSubscriber() {
            stopMePlease.unsubscribe();
            stopMePlease = null;
        }
    }
}
