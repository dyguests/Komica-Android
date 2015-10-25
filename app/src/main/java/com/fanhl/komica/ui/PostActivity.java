package com.fanhl.komica.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.komica.R;
import com.fanhl.komica.adapter.ReplyAdapter;
import com.fanhl.komica.api.PostApi;
import com.fanhl.komica.model.Post;
import com.fanhl.komica.model.Reply;
import com.fanhl.komica.model.Topic;
import com.fanhl.komica.ui.common.BaseActivity;
import com.fanhl.util.GsonUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

/**
 * 帖子页面
 */
public class PostActivity extends BaseActivity {
    public static final String TAG = PostActivity.class.getSimpleName();

    public static final String EXTRA_TOPIC = "EXTRA_TOPIC";
    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MenuItem refreshMenuItem;

    /**
     * 输入数据(根据此数据加载内容)
     */
    private Topic topic;

    ReplyAdapter replyAdapter;
    /**
     * (帖子的)回复
     */
    List<Reply>  replies;

    public static void launch(Activity activity, Topic data) {
        Intent intent = new Intent(activity, PostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_TOPIC, GsonUtil.json(data));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        topic = GsonUtil.obj(intent.getStringExtra(EXTRA_TOPIC), Topic.class);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        //custom

        assignViews();

        refreshData();
    }

    private void assignViews() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        replies = new ArrayList<>();
        replies.add(parse(topic));

        replyAdapter = new ReplyAdapter(this, replies);
        mRecyclerView.setAdapter(replyAdapter);
        replyAdapter.notifyDataSetChanged();
        replyAdapter.setOnItemClickListener((holder, position) -> {
            ReplyAdapter.ViewHolder viewHolder = (ReplyAdapter.ViewHolder) holder;
            //如果已经是详细图片,则直接跳转到图库页面.
            if (viewHolder.isImageDetail) {
                GalleryActivity.launch(PostActivity.this, viewHolder.item.getImgDetailUrl());
            } else {
                Picasso.with(PostActivity.this)
                        .load(viewHolder.item.getImgDetailUrl())
                        .placeholder(viewHolder.mImageView.getDrawable())
                        .into(viewHolder.mImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                viewHolder.isImageDetail = true;
                                GalleryActivity.launch(PostActivity.this, viewHolder.item.getImgDetailUrl());
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(mRecyclerView, R.string.load_img_detail_fail, Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    /**
     * 将话题(Topic,楼主的内容)转换成回复类型
     *
     * @param topic
     * @return
     */
    private Reply parse(Topic topic) {
        return new Reply(topic.getImgUrl(), topic.getImgDetailUrl(), topic.getContent());
    }

    private void refreshData() {
        Observable.create(new Observable.OnSubscribe<Post>() {
            @Override
            public void call(Subscriber<? super Post> subscriber) {
                subscriber.onNext(PostApi.getPost(topic));
                subscriber.onCompleted();
            }
        }).subscribeOn(HandlerScheduler.from(backgroundHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(post -> {
                    if (post != null && !post.getReplies().isEmpty()) {
                        this.replies.clear();
                        this.replies.addAll(post.getReplies());
                        replyAdapter.notifyDataSetChanged();
                        Log.d(TAG, "加载topics成功");
                    } else {
                        Log.e(TAG, "加载topics失败");
                        Snackbar.make(mRecyclerView, R.string.load_data_fail, Snackbar.LENGTH_LONG).show();
                    }
                    refreshMenuItem.setEnabled(true);
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);
        refreshMenuItem = menu.getItem(0);
        refreshMenuItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            item.setEnabled(false);
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
