package com.fanhl.komica.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 主菜单分类
 * Created by fanhl on 15/10/24.
 */
public class BBSMenuCategory {
    /**
     * 分类
     */
    String category;

    List<BBSMenuItem> items;

    public BBSMenuCategory(String sectionName) {
        this.category = sectionName;
        items = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<BBSMenuItem> getItems() {
        return items;
    }

    public void setItems(List<BBSMenuItem> items) {
        this.items = items;
    }
}
