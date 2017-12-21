package com.dab.zx.ui.deck;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dab.zx.R;
import com.dab.zx.bean.DeckPreviewBean;
import com.dab.zx.game.DeckManager;
import com.dab.zx.game.utils.DeckUtils;
import com.dab.zx.ui.base.BaseActivity;
import com.dab.zx.ui.base.BaseRecyclerViewListener;
import com.dab.zx.uitls.FileUtils;
import com.dab.zx.uitls.IntentUtils;
import com.dab.zx.uitls.JsonUtils;
import com.dab.zx.view.dialog.DialogConfirm;
import com.dab.zx.view.dialog.DialogEditText;
import com.dab.zx.view.widget.AppBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckPreviewActivity extends BaseActivity implements BaseRecyclerViewListener.OnItemClickListener, BaseRecyclerViewListener.OnItemLongClickListener {
    @BindView(R.id.viewContent)
    View         viewContent;
    @BindView(R.id.rv_deckpreview)
    RecyclerView rvDeckPreview;
    @BindView(R.id.viewAppBar)
    AppBarView   viewAppBar;

    @BindArray(R.array.deck_operation)
    String[] arrDeckOperation;

    private static final String TAG = DeckPreviewActivity.class.getSimpleName();

    private DeckPreviewAdapter mDeckPreviewAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deck_preview;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);
        viewAppBar.setNavigationClickListener(super::onBackPressed);
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
        Observable.just(DeckUtils.getDeckPreviewList()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(deckPreviewList -> {
            mDeckPreviewAdapter.updateData(deckPreviewList);
        });
    }

    /**
     * 卡组添加
     */
    @OnClick(R.id.fab_add)
    public void onAdd_Click() {
        new DialogEditText(this, getString(R.string.deck_pre_name), null, getString(R.string.deck_pre_name_hint), (dialog, deckName) -> {
            boolean checkDeckName = DeckUtils.checkDeckName(deckName);
            if (!checkDeckName) {
                FileUtils.writeFile(JsonUtils.serializer(new ArrayList<String>()), DeckUtils.getDeckPath(deckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, getString(R.string.add_succeed));
            } else {
                showSnackBar(viewContent, getString(R.string.deck_name_exits));
            }
        }).show();
    }

    @Override
    public void onItemClick(View view, List<?> data, int position) {
        if (view.getId() == R.id.btn_deck_editor) {
            showDeckEditor(data, position);
        } else {
            DeckEditorActivity.deckPreviewBean = (DeckPreviewBean)data.get(position);
            IntentUtils.gotoActivity(this, DeckEditorActivity.class);
        }
    }

    @Override
    public void onItemLongClick(View view, List<?> data, int position) {
        showDeckEditor(data, position);
    }

    private void showDeckEditor(List<?> data, int position) {
        new AlertDialog.Builder(this).setItems(arrDeckOperation, (dialog, which) -> {
            dialog.dismiss();
            switch (arrDeckOperation[which]) {
                case "确认":
                    confirmDeck(data, position);
                    break;
                case "更名":
                    renameDeck(data, position);
                    break;
                case "另存":
                    copyDeck(data, position);
                    break;
                case "删除":
                    deleteDeck(data, position);
                    break;
            }
        }).show();
    }

    /**
     * 确认卡组
     *
     * @param data     数据源
     * @param position 数据位置
     */
    private void confirmDeck(List<?> data, int position) {
        DeckPreviewBean deckPreview = (DeckPreviewBean)data.get(position);
        DeckConfirmActivity.mDeckManager = new DeckManager(deckPreview.getDeckName(), deckPreview.getNumberExList());
        IntentUtils.gotoActivity(this, DeckConfirmActivity.class);
    }

    /**
     * 重命名卡组
     *
     * @param data     数据源
     * @param position 数据位置
     */
    private void renameDeck(List<?> data, int position) {
        String deckName = ((DeckPreviewBean)data.get(position)).getDeckName();
        new DialogEditText(this, getString(R.string.deck_pre_name), deckName, getString(R.string.deck_pre_name_hint), (dialog, newDeckName) -> {
            boolean checkDeckName = deckName.equals(newDeckName) || !DeckUtils.checkDeckName(newDeckName);
            if (checkDeckName) {
                FileUtils.renameFile(DeckUtils.getDeckPath(deckName), DeckUtils.getDeckPath(newDeckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, getString(R.string.update_succeed));
            } else {
                showSnackBar(viewContent, getString(R.string.deck_name_exits));
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
        new DialogEditText(this, getString(R.string.deck_pre_name), deckName, getString(R.string.deck_pre_name_hint), (dialog, newDeckName) -> {
            boolean checkDeckName = DeckUtils.checkDeckName(newDeckName);
            if (!checkDeckName) {
                FileUtils.copyFile(DeckUtils.getDeckPath(deckName), DeckUtils.getDeckPath(newDeckName));
                mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
                dialog.dismiss();
                showSnackBar(viewContent, getString(R.string.copy_succeed));
            } else {
                showSnackBar(viewContent, getString(R.string.deck_name_exits));
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
        if (DialogConfirm.show(this, String.format("%s:%s?", getString(R.string.delete_confirm), deckName))) {
            FileUtils.deleteFile(DeckUtils.getDeckPath(deckName));
            mDeckPreviewAdapter.updateData(DeckUtils.getDeckPreviewList());
            showSnackBar(viewContent, getString(R.string.delete_succeed));
        }
    }
}
