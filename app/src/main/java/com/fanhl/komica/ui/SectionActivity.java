package com.fanhl.komica.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.komica.R;
import com.fanhl.komica.adapter.TopicAdapter;
import com.fanhl.komica.api.SectionApi;
import com.fanhl.komica.model.BBSMenuItem;
import com.fanhl.komica.model.Section;
import com.fanhl.komica.model.Topic;
import com.fanhl.komica.ui.common.BaseActivity;
import com.fanhl.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

/**
 * 版块内容页面
 */
public class SectionActivity extends BaseActivity {
    public static final String TAG = SectionActivity.class.getSimpleName();

    public static final String EXTRA_BBS_MENU = "EXTRA_BBS_MENU";

    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.recyclerView)
    RecyclerView         mRecyclerView;

    private MenuItem refreshMenuItem;

    /**
     * 输入数据(根据此数据加载内容)
     */
    private BBSMenuItem bbsMenuItem;

    private TopicAdapter topicAdapter;
    private List<Topic>  topics;

    public static void launch(Activity activity, BBSMenuItem data) {
        Intent intent = new Intent(activity, SectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_BBS_MENU, GsonUtil.json(data));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bbsMenuItem = GsonUtil.obj(intent.getStringExtra(EXTRA_BBS_MENU), BBSMenuItem.class);

        if (bbsMenuItem != null) {
            setTitle(bbsMenuItem.getName());
        }

        fab.setOnClickListener(view -> Snackbar.make(view, "新建帖子失败(还没做呢!)", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        //custom

        assignViews();

        refreshData();
    }

    private void assignViews() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        topics = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topics);
        mRecyclerView.setAdapter(topicAdapter);
        topicAdapter.setOnItemClickListener((holder, position) -> {
            PostActivity.launch(SectionActivity.this, ((TopicAdapter.ViewHolder) holder).item);
        });
    }

    private void refreshData() {
        Observable.create(new Observable.OnSubscribe<Section>() {
            @Override
            public void call(Subscriber<? super Section> subscriber) {
                subscriber.onNext(SectionApi.getTopics(bbsMenuItem));
                subscriber.onCompleted();
            }
        }).subscribeOn(HandlerScheduler.from(backgroundHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(section -> {
                    if (section != null && !section.getTopics().isEmpty()) {
                        this.topics.clear();
                        this.topics.addAll(section.getTopics());
                        topicAdapter.notifyDataSetChanged();
                        Log.d(TAG, "加载topics成功");
                    } else {
                        Log.e(TAG, "加载topics失败");
                        Snackbar.make(mRecyclerView, "加载数据失败,请重试.", Snackbar.LENGTH_LONG).show();
                    }
                    refreshMenuItem.setEnabled(true);
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.section, menu);
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
