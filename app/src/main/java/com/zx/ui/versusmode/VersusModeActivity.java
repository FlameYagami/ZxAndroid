package com.zx.ui.versusmode;

import android.view.View;

import com.zx.R;
import com.zx.config.MyApp;
import com.zx.event.EnterRoomEvent;
import com.zx.game.message.ModBus;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.versuroom.VersusRoomActivity;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.RxBus;
import com.zx.view.dialog.DialogEditText;
import com.zx.view.dialog.DialogVersusPersonal;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/2/8.
 */

public class VersusModeActivity extends BaseActivity
{

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
        MyApp.mMessageManager.start();

        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(EnterRoomEvent.class).subscribe(this::onEnterRoomResult));
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
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

    private void onEnterRoomResult(EnterRoomEvent mEnterRoomEvent) {
        hideDialog();
        switch (mEnterRoomEvent.getPlayerType()) {
            case 0:
                mDialogVersusPersonal.dismiss();
                break;
            case 1:
            case 2:
                mDialogJoinRoom.dismiss();
                break;
        }
        VersusRoomActivity.mEnterRoomEvent = mEnterRoomEvent;
        IntentUtils.gotoActivity(this, VersusRoomActivity.class);
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
                MyApp.mMessageManager.sendMessage(ModBus.onCreateRoom("火焰"));
            }
        });
        mDialogVersusPersonal.show();
    }

    /**
     * 加入房间事件
     */
    private void onJoinRoom() {
        mDialogJoinRoom = new DialogEditText(this, getString(R.string.join_room), "", "请输入房间号", (dialog, context) -> {
            MyApp.mMessageManager.sendMessage(ModBus.onJoinRoom(context));
        });
        mDialogJoinRoom.show();
    }

    @Override
    protected void onDestroy() {
        MyApp.mMessageManager.end();
        super.onDestroy();
    }
}
