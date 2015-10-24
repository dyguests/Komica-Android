package com.fanhl.komica.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 版块
 * <p>
 * 存放版块内容页面的数据
 * Created by fanhl on 15/10/24.
 */
public class Section {
    private final String category;
    private final String name;
    private final String url;

    /**
     * 添加话题 相关(新建档案)
     */
    //    add topic

    /**
     * 一页的所有话题(档)
     */
    List<Topic> topics;

    public Section(String category, String name, String url) {
        this.category = category;
        this.name = name;
        this.url = url;
        topics = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
