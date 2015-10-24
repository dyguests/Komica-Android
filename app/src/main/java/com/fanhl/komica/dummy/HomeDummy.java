package com.fanhl.komica.dummy;

import com.fanhl.komica.model.BBSMenuCategory;
import com.fanhl.komica.model.BBSMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanhl on 15/10/24.
 */
public class HomeDummy {
    public static List<BBSMenuCategory> getBBSMenu() {
        ArrayList<BBSMenuCategory> bbsMenuCategories = new ArrayList<>();

        BBSMenuCategory bbsMenuCategory = new BBSMenuCategory("連線板");

        bbsMenuCategory.getItems().add(new BBSMenuItem("連線板", "歡樂惡搞", "http://homu.komica.org/12/index.htm"));

        bbsMenuCategories.add(bbsMenuCategory);
        return bbsMenuCategories;
    }
}
