package com.fanhl.komica.model;

/**
 * Created by fanhl on 15/10/24.
 */
public class Topic {

    /**
     * 缩略图
     */
    private final String imgUrl;
    /**
     * 大图
     */
    private final String imgDetailUrl;
    /**
     * 内容
     */
    private final String content;
    /**
     * 详细页面连接
     */
    private final String detailUrl;

    public Topic(String topicUrl, String imgDetailUrl, String content, String detailUrl) {
        this.imgUrl = topicUrl;
        this.imgDetailUrl = imgDetailUrl;
        this.content = content;
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgDetailUrl() {
        return imgDetailUrl;
    }

    public String getContent() {
        return content;
    }

    public String getDetailUrl() {
        return detailUrl;
    }
}
