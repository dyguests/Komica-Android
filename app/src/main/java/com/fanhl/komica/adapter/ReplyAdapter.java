package com.fanhl.komica.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanhl.komica.R;
import com.fanhl.komica.adapter.common.AbsRecyclerAdapter;
import com.fanhl.komica.model.Reply;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/10/24.
 */
public class ReplyAdapter extends AbsRecyclerAdapter<ReplyAdapter.ViewHolder> {
    public static final String TAG = ReplyAdapter.class.getSimpleName();

    private final Context     context;
    private final List<Reply> list;

    public ReplyAdapter(Context context, List<Reply> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_reply_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Reply item = list.get(position);
        holder.bind(context, item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_item_topic_card.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class ViewHolder extends AbsRecyclerAdapter.ClickableViewHolder {
        @Bind(R.id.card_view)
        CardView mCardView;
        @Bind(R.id.imageView)
        public ImageView mImageView;
        @Bind(R.id.content)
        AppCompatTextView mContent;

        public Reply item;

        /**
         * 当前是否显示的是详细图片(否则为缩略图)
         */
        public boolean isImageDetail = false;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        private void bind(Context context, Reply item) {
            if (item.getImgUrl() != null) {
                Picasso.with(context)
                        .load(item.getImgUrl())
                        .placeholder(R.drawable.place_holder_img)
                        .fit()
                        .into(mImageView);
            } else {
                mImageView.setVisibility(View.GONE);
            }
            mContent.setText(item.getContent());

            this.item = item;
        }
    }
}
