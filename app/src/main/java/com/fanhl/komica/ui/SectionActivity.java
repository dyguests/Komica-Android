package com.fanhl.komica.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.komica.GsonUtil;
import com.fanhl.komica.R;
import com.fanhl.komica.api.SectionApi;
import com.fanhl.komica.model.BBSMenuItem;
import com.fanhl.komica.model.Section;
import com.fanhl.komica.ui.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

public class SectionActivity extends BaseActivity {
    public static final String TAG = SectionActivity.class.getSimpleName();

    public static final String EXTRA_BBSMENU = "EXTRA_BBSMENU";

    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private MenuItem refreshMenuItem;

    private BBSMenuItem bbsMenuItem;

    public static void launch(Activity activity, BBSMenuItem data) {
        Intent intent = new Intent(activity, SectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_BBSMENU, GsonUtil.json(data));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bbsMenuItem = GsonUtil.obj(intent.getStringExtra(EXTRA_BBSMENU), BBSMenuItem.class);

        if (bbsMenuItem != null) {
            setTitle(bbsMenuItem.getName());
        }

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        //custom

        assignViews();

        refreshData();
    }

    private void assignViews() {

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
                .subscribe(topics -> {
                    if (topics != null) {
//                        this.bbsSections.clear();
//                        this.bbsSections.addAll(bbsSections);
//                        bbsMenuAdapter.notifyDataSetChanged();
                        Log.d(TAG, "加载topics成功");
                    } else {
                        Log.e(TAG, "加载topics失败");
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
