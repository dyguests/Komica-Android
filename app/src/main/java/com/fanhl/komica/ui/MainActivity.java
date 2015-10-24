package com.fanhl.komica.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.komica.R;
import com.fanhl.komica.adapter.BBSMenuAdapter;
import com.fanhl.komica.api.HomeApi;
import com.fanhl.komica.model.BBSMenuCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

public class MainActivity extends com.fanhl.komica.ui.common.BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar        toolbar;
    //    @Bind(R.id.fab)
//    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout   drawer;
    @Bind(R.id.recyclerView)
    RecyclerView   bbsMenuRecyclerView;

    private MenuItem refreshMenuItem;

    private BBSMenuAdapter        bbsMenuAdapter;
    private List<BBSMenuCategory> bbsSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

//        fab.setOnClickListener(v -> refreshData());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //custom

        assignViews();
        refreshData();
    }

    private void assignViews() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        bbsMenuRecyclerView.setLayoutManager(mLayoutManager);
        bbsMenuRecyclerView.setHasFixedSize(true);

        bbsSections = new ArrayList<>();
        bbsMenuAdapter = new BBSMenuAdapter(this, bbsSections);
        bbsMenuRecyclerView.setAdapter(bbsMenuAdapter);
        bbsMenuAdapter.setOnItemClickListener((holder, position) -> {
            SectionActivity.launch(MainActivity.this, ((BBSMenuAdapter.ViewHolder) holder).item);
        });
    }

    /**
     * 初始化菜单
     */
    private void refreshData() {
        Observable.create(new Observable.OnSubscribe<List<BBSMenuCategory>>() {
            @Override
            public void call(Subscriber<? super List<BBSMenuCategory>> subscriber) {
                subscriber.onNext(HomeApi.getBBSMenu());
                subscriber.onCompleted();
            }
        }).subscribeOn(HandlerScheduler.from(backgroundHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bbsSections -> {
                    if (bbsSections != null) {
                        this.bbsSections.clear();
                        this.bbsSections.addAll(bbsSections);
                        bbsMenuAdapter.notifyDataSetChanged();
                        Log.d(TAG, "加载bbsMenu成功");
                    } else {
                        Log.e(TAG, "加载bbsMenu失败");
                    }
                    refreshMenuItem.setEnabled(true);
                }, Throwable::printStackTrace);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        //主界面按后退不退出程序 // FIXME: 15/10/24 之前改成按两次退出
//        return super.onOptionsItemSelected(item);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
