package com.zx.ui.versusmode;

import android.view.View;
import android.widget.LinearLayout;

import com.michaelflisar.rxbus2.RxBusBuilder;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;
import com.zx.R;
import com.zx.config.MyApp;
import com.zx.event.JoinGameEvent;
import com.zx.game.enums.PlayerType;
import com.zx.game.message.ModBusCreator;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.versuroom.VersusRoomActivity;
import com.zx.uitls.IntentUtils;
import com.zx.view.dialog.DialogEditText;
import com.zx.view.dialog.DialogVersusPersonal;
import com.zx.view.widget.AppBarView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/2/8.
 */

public class VersusModeActivity extends BaseActivity
{
    @BindView(R.id.view_content)
    LinearLayout viewContent;
    @BindView(R.id.viewAppBar)
    AppBarView   viewAppBar;

    private static final String TAG = VersusModeActivity.class.getSimpleName();

    DialogVersusPersonal mDialogVersusPersonal;
    DialogEditText       mDialogJoinRoom;

    @Override
    public int getLayoutId() {
        return R.layout.activity_versus_mode;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
        MyApp.Client.initPlayer(String.valueOf(new Random().nextInt()));
        MyApp.Client.start();
        RxDisposableManager.addDisposable(this, RxBusBuilder.create(JoinGameEvent.class).subscribe(this::onJoinRoom));
    }

    @OnClick({R.id.view_ladder_tournament, R.id.view_versus_freedom, R.id.view_versus_personal})
    public void onMode_Click(View view) {
        switch (view.getId()) {
            case R.id.view_ladder_tournament:
                break;
            case R.id.view_versus_freedom:
                break;
            case R.id.view_versus_personal:
                onVersusPersonal();
                break;
        }
    }

    private void onJoinRoom(JoinGameEvent mJoinGameEvent) {
        hideDialog();
        if (mJoinGameEvent.getPlayerType() == PlayerType.Host) {
            mDialogVersusPersonal.dismiss();
            if (mJoinGameEvent.isSucceed()) {
                IntentUtils.gotoActivity(this, VersusRoomActivity.class);
            } else {
                showToast("服务器房间队列已满,请稍后再试");
            }
        } else {
            mDialogJoinRoom.dismiss();
            if (mJoinGameEvent.isSucceed()) {
                IntentUtils.gotoActivity(this, VersusRoomActivity.class);
            } else {
                showToast("该房间不存在");
            }
        }
    }

    /**
     * 私人对战事件
     */
    private void onVersusPersonal() {
        mDialogVersusPersonal = new DialogVersusPersonal(this, (dialog, type) -> {
            if (type.equals(DialogVersusPersonal.ButtonType.JoinRoom)) {
                dialog.dismiss();
                onJoinRoom();
            } else if (type.equals(DialogVersusPersonal.ButtonType.CreateRoom)) {
                showDialog("");
                MyApp.Client.send(ModBusCreator.onCreateRoom(MyApp.Client.Player));
            }
        });
        mDialogVersusPersonal.show();
    }

    /**
     * 加入房间事件
     */
    private void onJoinRoom() {
        mDialogJoinRoom = new DialogEditText(this, getString(R.string.join_room), "", "请输入房间号", (dialog, content) -> {
            MyApp.Client.send(ModBusCreator.onJoinRoom(content, MyApp.Client.Player));
            showDialog("");
        });
        mDialogJoinRoom.show();
    }

    @Override
    protected void onDestroy() {
        MyApp.Client.finish();
        super.onDestroy();
    }
}
