package com.dab.zx.ui.versus;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dab.zx.R;
import com.dab.zx.bean.DuelBean;
import com.dab.zx.bean.HandBean;
import com.dab.zx.game.utils.DeckUtils;
import com.dab.zx.ui.base.BaseExActivity;
import com.dab.zx.uitls.MyPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.dab.zx.uitls.PathManager.pictureDir;

/**
 * Created by 八神火焰 on 2017/1/13.
 */

public class VersusActivity extends BaseExActivity {
    @BindView(R.id.rv_red_hand)
    RecyclerView rvRedHand;

    private HandAdapter     mGreenHandAdapter     = new HandAdapter(this);
    private ResourceAdapter mGreenResourceAdapter = new ResourceAdapter(this);
    private List<HandBean>  mGreenHandBean        = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_duel;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        List<String> numberExList = DeckUtils.getDeckPreviewList().get(0).getNumberExList();
        mGreenHandBean.addAll(stream(numberExList).select(numberEx -> new HandBean(new DuelBean(numberEx, pictureDir + File.separator + numberEx + getString(R.string.image_extension)))).toList());
        rvRedHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRedHand.setAdapter(mGreenHandAdapter);
        mGreenHandAdapter.setOnItemClickListener((view, data, position) -> {
            new MyPopup(this, R.layout.popupwindow_hand);
        });
        mGreenHandAdapter.updateData(mGreenHandBean);
    }
}
