package com.fanhl.komica.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by fanhl on 15/10/24.
 */
public abstract class AbsRecyclerAdapter<BH extends AbsRecyclerAdapter.ClickableViewHolder> extends RecyclerView.Adapter<BH> {
    protected OnItemClickListener     itemClickListener;
    protected OnItemLongClickListener itemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(AbsRecyclerAdapter.ClickableViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(AbsRecyclerAdapter.ClickableViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    @Override
    public void onBindViewHolder(BH holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (itemLongClickListener != null) {
                return itemLongClickListener.onItemLongClick(holder, position);
            } else {
                return false;
            }
        });
    }

    public class ClickableViewHolder extends RecyclerView.ViewHolder {

//        private View parentView;

        public ClickableViewHolder(View itemView) {
            super(itemView);
//            this.parentView = itemView;
        }

//        public View getParentView() {
//            return parentView;
//        }

    }
}
