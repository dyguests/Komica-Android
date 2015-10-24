package com.fanhl.komica.model;

/**
 * 主菜单项
 * Created by fanhl on 15/10/24.
 */
public class BBSMenuItem {
    /**
     * 分类
     */
    String category;
    /**
     * 菜单项
     */
    String name;
    /**
     * 跳转地址
     */
    String url;

    public BBSMenuItem(String sectionName, String menuName, String url) {
        this.category = sectionName;
        this.name = menuName;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
