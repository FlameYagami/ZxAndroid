package com.zx.ui.lan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.R;
import com.zx.config.SpConst;
import com.zx.service.ClientService;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.SpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class LanActivity extends BaseActivity
{
    @BindView(R.id.view_content)
    RelativeLayout viewContent;
    @BindView(R.id.txt_nickname)
    TextView       tvNickName;
    @BindView(R.id.recyclerview)
    RecyclerView   mRecyclerView;

    private static final String TAG = LanActivity.class.getSimpleName();

    LanAdapter mLanAdapter = new LanAdapter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_lan;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        IntentUtils.startClientService(ClientService.class);
        tvNickName.setText(SpUtil.getInstances().getString(SpConst.NickName));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mLanAdapter.setOnItemClickListener(this::onJoinRoom);
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
        IntentUtils.sendBroadcast(this, ClientService.STOP_SERVICE);
    }

    @OnClick(R.id.btn_build_host)
    public void onBuildHost_Click() {
        Observable.just(tvNickName.getText().toString().trim()).subscribe(nickName -> {
            if (TextUtils.isEmpty(nickName)) {
                showSnackBar(viewContent, getString(R.string.nickname_hint));
                return;
            }
            SpUtil.getInstances().putString(SpConst.NickName, nickName);
        });
    }

    /**
     * 加入房间
     */
    private void onJoinRoom(View view, List<?> data, int position){

    }
}
