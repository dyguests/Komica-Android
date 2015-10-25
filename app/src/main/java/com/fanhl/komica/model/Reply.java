package com.fanhl.komica.model;

/**
 * (帖子的)回复
 * Created by fanhl on 15/10/24.
 */
public class Reply {
    private final String imgUrl;
    private final String imgDetailUrl;
    private final String content;

    public Reply(String imgUrl, String imgDetailUrl, String content) {
        this.imgUrl = imgUrl;
        this.imgDetailUrl = imgDetailUrl;
        this.content = content;
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
}
