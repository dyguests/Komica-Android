package com.fanhl.komica.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子
 * Created by fanhl on 15/10/24.
 */
public class Post {

    //// FIXME: 15/10/24 回帖相关


    List<Reply> replies;

    public Post() {
        replies = new ArrayList<>();
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
