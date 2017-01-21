package com.zx.ui.deckpreview;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zx.R;
import com.zx.bean.DeckPreviewBean;
import com.zx.ui.base.BaseActivity;
import com.zx.ui.base.BaseRecyclerViewListener;
import com.zx.ui.deckeditor.DeckEditorActivity;
import com.zx.uitls.DeckUtils;
import com.zx.uitls.FileUtils;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.JsonUtils;
import com.zx.view.dialog.DialogEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckPreviewActivity extends BaseActivity implements BaseRecyclerViewListener.OnItemClickListener, BaseRecyclerViewListener.OnItemLongClickListener
{
    @BindView(R.id.view_content)
    CoordinatorLayout viewContent;
    @BindView(R.id.rv_deckpreview)
    RecyclerView      rvDeckPreview;
    @BindString(R.string.add_succeed)
    String            addSucceed;
    @BindString(R.string.update_succeed)
    String            updateSucceed;
    @BindString(R.string.delete_succeed)
    String            deleteSucceed;
    @BindString(R.string.deck_name_exits)
    String            deckNameExits;

    private static final String TAG = DeckPreviewActivity.class.getSimpleName();

    DeckPreviewAdapter mDeckPreviewAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deck_preview;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDeckPreviewAdapter = new DeckPreviewAdapter(this);
        rvDeckPreview.setLayoutManager(linearLayoutManager);
        rvDeckPreview.setAdapter(mDeckPreviewAdapter);

        mDeckPreviewAdapter.setOnItemClickListener(this);
        mDeckPreviewAdapter.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just(DeckUtils.getDeckPreviewList()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(deckPreviewBeanList -> {
            mDeckPreviewAdapter.updateData(deckPreviewBeanList);
        });
    }

    /**
     * 卡组添加
     */
    @OnClick(R.id.fab_add)
    public void onAdd_Click() {
        new DialogEditText(this, getString(R.string.deck_name), null, getString(R.string.deck_name_hint), (dialog, deckName) -> {
            boolean checkDeckName = DeckUtils.checkDeckName(deckName);
            if (!checkDeckName) {
                FileUtils.writeFile(JsonUtils.serializer(new ArrayList<String>()), DeckUtils.getDeckPath(deckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, addSucceed);
            } else {
                showSnackBar(viewContent, deckNameExits);
            }
        }).show();
    }

    @OnClick(R.id.img_back)
    public void onBack_Click() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(View view, List<?> data, int position) {
        DeckEditorActivity.deckPreviewBean = (DeckPreviewBean)data.get(position);
        IntentUtils.gotoActivity(this, DeckEditorActivity.class);
    }

    @Override
    public void onItemLongClick(View view, List<?> data, int position) {
        new AlertDialog.Builder(this).setItems(new String[]{"重命名", "卡组另存", "卡组删除"}, (dialog, which) -> {
            dialog.dismiss();
            if (which == 0) {
                renameDeck(data, position);
            } else if (which == 1) {
                copyDeck(data, position);
            } else {
                deleteDeck(data, position);
            }
        }).show();
    }

    /**
     * 重命名卡组
     *
     * @param data     数据源
     * @param position 数据位置
     */
    private void renameDeck(List<?> data, int position) {
        String deckName = ((DeckPreviewBean)data.get(position)).getDeckName();
        new DialogEditText(this, getString(R.string.deck_name), deckName, getString(R.string.deck_name_hint), (dialog, newDeckName) -> {
            boolean checkDeckName = deckName.equals(newDeckName) || !DeckUtils.checkDeckName(newDeckName);
            if (checkDeckName) {
                FileUtils.renameFile(DeckUtils.getDeckPath(deckName), DeckUtils.getDeckPath(newDeckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, updateSucceed);
            } else {
                showSnackBar(viewContent, deckNameExits);
            }
        }).show();
    }

    /**
     * 复制卡组
     *
     * @param data     数据源
     * @param position 数据位置
     */
    private void copyDeck(List<?> data, int position) {
        String deckName = ((DeckPreviewBean)data.get(position)).getDeckName();
        new DialogEditText(this, getString(R.string.deck_name), deckName, getString(R.string.deck_name_hint), (dialog, newDeckName) -> {
            boolean checkDeckName = DeckUtils.checkDeckName(newDeckName);
            if (!checkDeckName) {
                FileUtils.copyFile(DeckUtils.getDeckPath(deckName), DeckUtils.getDeckPath(newDeckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, getString(R.string.copy_succeed));
            } else {
                showSnackBar(viewContent, deckNameExits);
            }
        }).show();
    }

    /**
     * 删除卡组
     *
     * @param data     数据源
     * @param position 数据位置
     */
    private void deleteDeck(List<?> data, int position) {
        String deckName = ((DeckPreviewBean)data.get(position)).getDeckName();
        FileUtils.deleteFile(DeckUtils.getDeckPath(deckName));
        mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
        showSnackBar(viewContent, deleteSucceed);
    }
}
