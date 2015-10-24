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
import com.fanhl.komica.model.Topic;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/10/24.
 */
public class TopicAdapter extends AbsRecyclerAdapter<TopicAdapter.ViewHolder> {
    public static final String TAG = TopicAdapter.class.getSimpleName();

    private final Context     context;
    private final List<Topic> list;

    public TopicAdapter(Context context, List<Topic> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_topic_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Topic item = list.get(position);
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
        CardView          mCardView;
        @Bind(R.id.imageView)
        ImageView         mImageView;
        @Bind(R.id.content)
        AppCompatTextView mContent;

        public Topic item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        private void bind(Context context, Topic item) {
            Picasso.with(context)
                    .load(item.getTopicUrl())
                    .fit()
                    .into(mImageView);
            mContent.setText(item.getContent());

            this.item = item;
        }
    }
}
