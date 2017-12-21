package com.dab.zx.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected abstract int getLayoutId();

    protected abstract RecyclerView.ViewHolder getViewHolder(View view);

    protected abstract void getView(RecyclerView.ViewHolder holder, int position);

    protected Context context;
    protected List<?> data = new ArrayList<>();
    protected BaseRecyclerViewListener.OnItemClickListener     mOnItemClickListener;
    protected BaseRecyclerViewListener.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(BaseRecyclerViewListener.OnItemClickListener mListener) {
        this.mOnItemClickListener = mListener;
    }

    public void setOnItemLongClickListener(BaseRecyclerViewListener.OnItemLongClickListener mListener) {
        this.mOnItemLongClickListener = mListener;
    }

    protected BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<?> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<?> data, int position) {
        this.data = data;
        notifyItemInserted(position);
    }

    public void removeData(List<?> data, int position) {
        this.data = data;
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        getView(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
