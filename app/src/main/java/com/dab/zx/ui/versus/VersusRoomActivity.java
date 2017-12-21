package com.dab.zx.ui.versus;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.dab.zx.config.MyApp;
import com.dab.zx.event.DuelistStateEvent;
import com.dab.zx.event.EnterGameEvent;
import com.dab.zx.event.LeaveGameEvent;
import com.dab.zx.event.StartGameEvent;
import com.dab.zx.game.DeckManager;
import com.dab.zx.game.Player;
import com.dab.zx.game.enums.PlayerType;
import com.dab.zx.game.message.ModBusCreator;
import com.dab.zx.ui.base.BaseExActivity;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.view.dialog.DialogDeckPreview;
import com.dab.zx.view.widget.AppBarView;
import com.michaelflisar.rxbus2.RxBusBuilder;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;

import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class VersusRoomActivity extends BaseExActivity implements AppBarView.NavigationClickListener {
    @BindString(R.string.host)
    String host;
    @BindString(R.string.guest)
    String guest;
    @BindString(R.string.ready)
    String ready;
    @BindString(R.string.cancel)
    String notReady;

    @BindView(R.id.viewContent)
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
    @BindView(R.id.viewAppBar)
    AppBarView     viewAppBar;

    private UISubscriber mUISubscriber;
    private byte         ownerType;
    private DeckManager  mDeckManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_versus_room;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(this);
        // 选手进入房间事件注册
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(EnterGameEvent.class).subscribe(this::onEnterRoom));
        // 选手状态事件注册
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(DuelistStateEvent.class).subscribe(this::onDuelistState));
        // 离开房间事件注册
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(LeaveGameEvent.class).subscribe(this::onLeaveRoom));
        // 开始游戏事件注册
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(StartGameEvent.class).subscribe(this::onStartGame));

        ownerType = MyApp.Client.Player.getType();
        tvRoomId.setText(MyApp.Client.Room.getRoomId());
    }

    @Override
    protected void onResume() {
        mUISubscriber = new UISubscriber();
        super.onResume();
    }


    /**
     * 卡组选择
     */
    @OnClick({R.id.img_deck_player0, R.id.img_deck_player1})
    public void onDeckOwner_Click(View view) {
        if (view.getId() == imgDeck[ownerType].getId()) {
            new DialogDeckPreview(this, (dialog, bean) -> {
                Glide.with(this).load(bean.getPlayerPath()).into(imgDeck[ownerType]);
                tvDeckName[ownerType].setText(bean.getDeckName());
                mDeckManager = new DeckManager(bean.getDeckName(), bean.getNumberExList());
            }).show();
        }
    }

    /**
     * 准备状态
     */
    @OnClick({R.id.btn_ready_player0, R.id.btn_ready_player1})
    public void onReady_Click(View view) {
        if (view.getId() == btnReady[ownerType].getId()) {
            if (TextUtils.isEmpty(tvDeckName[ownerType].getText())) {
                showToast("请选择卡组");
            } else {
                showDialog("");
                MyApp.Client.send(ModBusCreator.onPlayerState(MyApp.Client.Player));
            }
        }
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
    private void onEnterRoom(EnterGameEvent mEnterGameEvent) {
        showToast(mEnterGameEvent.getPlayer().getName() + "进入了房间");
    }

    /**
     * 离开房间事件
     */
    private void onLeaveRoom(LeaveGameEvent mLeaveGameEvent) {
        if (mLeaveGameEvent.getOwnerType() == mLeaveGameEvent.getPlayer().getType()) {
            super.onBackPressed();
        } else {
            if (0 == mLeaveGameEvent.getPlayer().getType()) {
                showToast(mLeaveGameEvent.getPlayer().getName() + "撤销了房间");
            } else {
                showToast(mLeaveGameEvent.getPlayer().getName() + "离开了房间");
            }
        }
    }

    /**
     * 选手状态改变
     */
    private void onDuelistState(DuelistStateEvent mDuelistStateEvent) {
        hideDialog();
        if (2 == stream(MyApp.Client.Game.getDuelists()).where(Player::isReady).count() && PlayerType.Host == ownerType) {
            showDialog("");
            MyApp.Client.send(ModBusCreator.onStartGame(mDeckManager));
        }
    }

    /**
     * 开始游戏
     */
    private void onStartGame(StartGameEvent mStartGameEvent) {
        hideDialog();
        IntentUtils.gotoActivity(this, VersusActivity.class);
    }

    /**
     * 更新界面
     */
    private void updateUI() {
        if (null != MyApp.Client.Game) {
            Player[] players = MyApp.Client.Game.getDuelists();
            for (int i = 0; i != 2; i++) {
                if (null == players[i]) {
                    viewPlayer[i].setVisibility(View.GONE);
                    viewHintPlayer[i].setVisibility(View.VISIBLE);
                    return;
                }
                viewPlayer[i].setVisibility(View.VISIBLE);
                viewHintPlayer[i].setVisibility(View.GONE);
                tvType[i].setText(players[i].getType() == PlayerType.Host ? host : guest);
                tvName[i].setText(players[i].getName());
                btnReady[i].setText(players[i].isReady() ? notReady : ready);
                btnReady[i].setEnabled(players[i].getType() == ownerType);
            }
        }
    }

    @Override
    public void onNavigationClick() {

    }

    /**
     * 界面轮询
     */
    private class UISubscriber {
        Disposable disposable;

        UISubscriber() {
            disposable = Observable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> updateUI());
        }

        void releaseSubscriber() {
            disposable.dispose();
            disposable = null;
        }
    }
}
