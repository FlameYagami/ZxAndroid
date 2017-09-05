package com.zx.ui.setting;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.zx.R;
import com.zx.bean.KeySearchBean;
import com.zx.config.SpConst;
import com.zx.ui.base.BaseActivity;
import com.zx.uitls.SpUtils;
import com.zx.uitls.StringUtils;
import com.zx.view.dialog.DialogCheckBox;
import com.zx.view.widget.AppBarView;
import com.zx.view.widget.MessageView;

import butterknife.BindArray;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

public class AdvancedActivity extends BaseActivity
{
    @BindView(R.id.msg_preview_order)
    MessageView viewOrderPattern;
    @BindView(R.id.msg_key_search)
    MessageView viewKeySearch;
    @BindView(R.id.viewAppBar)
    AppBarView  viewAppBar;
    @BindView(R.id.msg_deck_confirm_span_count)
    MessageView viewDeckConfirmSpanCount;
    @BindView(R.id.msg_deck_editor_span_count)
    MessageView viewDeckEditorSpanCount;

    @BindArray(R.array.preview_order)
    String[] mPreOrder;
    @BindArray(R.array.span_count)
    String[] mSpanCount;
    @BindInt(R.integer.advanced_deck_editor_default_span_count)
    int      mDeckEditorDefaultSpanCount;
    @BindInt(R.integer.advanced_deck_confirm_default_span_count)
    int      mDeckConfirmDefaultSpanCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_advanced;
    }

    @Override
    public void initViewAndData() {
        ButterKnife.bind(this);

        viewAppBar.setNavigationClickListener(super::onBackPressed);
        viewKeySearch.setValue(StringUtils.changeList2String(KeySearchBean.getSelectKeySearchList()));
        String previewOrder         = SpUtils.getString(SpConst.PreviewOrderPattern);
        int    deckEditorSpanCount  = SpUtils.getInt(SpConst.DeckEditorSpanCount);
        int    deckConfirmSpanCount = SpUtils.getInt(SpConst.DeckConfirmSpanCount);
        previewOrder = TextUtils.isEmpty(previewOrder) ? mPreOrder[0] : previewOrder;
        deckEditorSpanCount = deckEditorSpanCount == -1 ? mDeckEditorDefaultSpanCount : deckEditorSpanCount;
        deckConfirmSpanCount = deckConfirmSpanCount == -1 ? mDeckConfirmDefaultSpanCount : deckConfirmSpanCount;
        viewOrderPattern.setStringSp(SpConst.PreviewOrderPattern, previewOrder);
        viewDeckEditorSpanCount.setIntSp(SpConst.DeckEditorSpanCount, deckEditorSpanCount);
        viewDeckConfirmSpanCount.setIntSp(SpConst.DeckConfirmSpanCount, deckConfirmSpanCount);
    }

    @OnClick(R.id.msg_preview_order)
    public void onOrderPattern_Click() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.preview_order))
                .setItems(mPreOrder, (view, index)
                        -> viewOrderPattern.setStringSp(SpConst.PreviewOrderPattern, mPreOrder[index]))
                .show();
    }

    @OnClick(R.id.msg_key_search)
    public void onKeySearch_Click() {
        new DialogCheckBox(this, getString(R.string.key_search), KeySearchBean.getKeySearchMap(), keySearchMap -> {
            KeySearchBean.saveKeySearchMap(keySearchMap);
            viewKeySearch.setValue(StringUtils.changeList2String(KeySearchBean.getSelectKeySearchList()));
        }).show();
    }

    @OnClick(R.id.msg_deck_confirm_span_count)
    public void onDeckConfirmSpanCount_Click() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.advanced_deck_confirm_span_count))
                .setItems(mSpanCount, (view, index)
                        -> viewDeckConfirmSpanCount.setIntSp(SpConst.DeckConfirmSpanCount, Integer.valueOf(mSpanCount[index])))
                .show();
    }

    @OnClick(R.id.msg_deck_editor_span_count)
    public void onDeckEditorSpanCount_Click() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.advanced_deck_editor_span_count))
                .setItems(mSpanCount, (view, index)
                        -> viewDeckEditorSpanCount.setIntSp(SpConst.DeckEditorSpanCount, Integer.valueOf(mSpanCount[index])))
                .show();
    }
}
