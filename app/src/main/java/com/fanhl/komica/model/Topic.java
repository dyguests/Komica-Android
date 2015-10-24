package com.fanhl.komica.model;

/**
 * Created by fanhl on 15/10/24.
 */
public class Topic {

    private final String topicUrl;
    private final String content;
    private final String detailUrl;

    public Topic(String topicUrl, String content, String detailUrl) {
        this.topicUrl = topicUrl;
        this.content = content;
        this.detailUrl = detailUrl;
    }
}
