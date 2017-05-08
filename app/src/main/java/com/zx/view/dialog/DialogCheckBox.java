package com.zx.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/16.
 */

public class DialogCheckBox extends AlertDialog implements DialogInterface.OnClickListener
{
    private static final String TAG = DialogCheckBox.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private CheckBoxAdapter mCheckBoxAdapter;

    public interface OnClickListener
    {
        void getCheckBoxMap(LinkedHashMap<String, Boolean> mCheckboxMap);
    }

    public DialogCheckBox(@NonNull Context context, String title, LinkedHashMap<String, Boolean> mCheckboxMap, OnClickListener mOnClickListener) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_checkbox, null);
        setView(view);
        setTitle(title);
        setButton(BUTTON_POSITIVE, "确 定", this);
        setButton(BUTTON_NEGATIVE, "取 消", this);
        this.mOnClickListener = mOnClickListener;
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_checkbox);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mCheckBoxAdapter = new CheckBoxAdapter(context, mCheckboxMap);
        mRecyclerView.setAdapter(mCheckBoxAdapter);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (which == BUTTON_POSITIVE) {
            mOnClickListener.getCheckBoxMap(mCheckBoxAdapter.getCheckboxMap());
        }
    }

    private class CheckBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private LayoutInflater                 layoutInflater;
        private LinkedHashMap<String, Boolean> mCheckboxMap;
        private List<String> mKey = new ArrayList<>();

        CheckBoxAdapter(Context context, LinkedHashMap<String, Boolean> mCheckboxMap) {
            layoutInflater = LayoutInflater.from(context);
            this.mCheckboxMap = mCheckboxMap;
            mKey.addAll(mCheckboxMap.keySet());
        }

        LinkedHashMap<String, Boolean> getCheckboxMap() {
            return mCheckboxMap;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_checkbox, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder)holder;
            String     key        = mKey.get(position);
            viewHolder.compatCheckBox.setText(key);
            viewHolder.compatCheckBox.setChecked(mCheckboxMap.get(key));
            viewHolder.compatCheckBox.setOnCheckedChangeListener((compoundButton, b) -> mCheckboxMap.put(key, !mCheckboxMap.get(key)));
        }

        @Override
        public int getItemCount() {
            return mCheckboxMap.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.checkbox)
        AppCompatCheckBox compatCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
