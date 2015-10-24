package com.fanhl.komica.api;

import com.fanhl.komica.common.C;
import com.fanhl.komica.common.U;
import com.fanhl.komica.dummy.HomeDummy;
import com.fanhl.komica.model.BBSMenuCategory;
import com.fanhl.komica.model.BBSMenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanhl on 15/10/24.
 */
public class HomeApi {
    public static List<BBSMenuCategory> getBBSMenu() {
        if (C.DUMMY) {
            return HomeDummy.getBBSMenu();
        }

        Document document = null;
        try {
            document = Jsoup.connect(U.KOMICA_HOME_MENU).timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document == null) {
            return null;
        }

        Element menus = document.select("font[size=2]").get(0);

        ArrayList<BBSMenuCategory> bbsSections = new ArrayList<>();
        BBSMenuCategory            bbsSection  = null;
        for (Element item : menus.getAllElements()) {
            String tagName = item.tagName();
            if (tagName.equals("b")) {
                bbsSection = new BBSMenuCategory(item.text());
                bbsSections.add(bbsSection);
            } else if (tagName.equals("a")) {
                if (bbsSection != null) {
                    bbsSection.getItems().add(new BBSMenuItem(bbsSection.getCategory(), item.text(), item.attr("href")));
                }
            }
        }

        return bbsSections;
    }
}
