package com.fanhl.komica.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanhl.komica.R;
import com.fanhl.komica.adapter.common.AbsRecyclerAdapter;
import com.fanhl.komica.model.BBSMenuCategory;
import com.fanhl.komica.model.BBSMenuItem;
import com.fanhl.komica.util.ColorGenerator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主菜单
 * Created by fanhl on 15/10/24.
 */
public class BBSMenuAdapter extends AbsRecyclerAdapter<BBSMenuAdapter.ViewHolder> {

    private final Context               context;
    private final List<BBSMenuCategory> list;

    private ColorGenerator mColorGenerator;

    public BBSMenuAdapter(Context context, List<BBSMenuCategory> list) {
        this.context = context;
        this.list = list;
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_menu_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BBSMenuItem item = getItem(position);
        if (item != null) {
            holder.bind(item, this.mColorGenerator.getColor(item.getCategory()));
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (BBSMenuCategory bbsSection : list) {
            count += bbsSection.getItems().size();
        }
        return count;
    }

    private BBSMenuItem getItem(int position) {
        for (BBSMenuCategory bbsSection : list) {
            List<BBSMenuItem> bbsMenus = bbsSection.getItems();
            int size = bbsMenus.size();
            if (position < size) {
                return bbsMenus.get(position);
            } else {
                position -= size;
            }
        }
        return null;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_item_menu_card.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class ViewHolder extends AbsRecyclerAdapter.ClickableViewHolder {
        @Bind(R.id.card_view)
        CardView     mCardView;
        @Bind((R.id.card_content))
        LinearLayout mCardContent;
        @Bind(R.id.title)
        TextView     mTitle;

        public BBSMenuItem item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        private void bind(BBSMenuItem item, int color) {
            mCardContent.setBackgroundColor(color);
            mTitle.setText(item.getName());

            this.item = item;
        }
    }
}
