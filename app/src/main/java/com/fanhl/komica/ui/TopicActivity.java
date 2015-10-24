package com.fanhl.komica.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fanhl.komica.R;
import com.fanhl.komica.model.Topic;
import com.fanhl.util.GsonUtil;

public class TopicActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC = "EXTRA_TOPIC";


    /**
     * 输入数据(根据此数据加载内容)
     */
    private Topic topic;


    public static void launch(Activity activity, Topic data) {
        Intent intent = new Intent(activity, SectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_TOPIC, GsonUtil.json(data));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        topic = GsonUtil.obj(intent.getStringExtra(EXTRA_TOPIC), Topic.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}
