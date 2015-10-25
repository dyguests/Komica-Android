package com.fanhl.komica.dummy;

import com.fanhl.komica.model.BBSMenuItem;
import com.fanhl.komica.model.Section;
import com.fanhl.komica.model.Topic;

/**
 * Created by fanhl on 15/10/24.
 */
public class SectionDummy {
    public static Section getTopics(BBSMenuItem bbsMenuItem) {
        Section section = new Section(bbsMenuItem.getCategory(), bbsMenuItem.getName(), bbsMenuItem.getUrl());

        String relativeUrl  = "index.php?res=1552412";
        String imgSrc       = "http://homu.komica.org/12/thumb/1445656900798s.jpg";
        String imgDetailUrl = null;
        String content      = "出大事了\n\n\n\n「フルメタル・パニック！」新作動畫確定";

        String preUrl    = section.getUrl();
        int    lastIndex = preUrl.lastIndexOf("/");
        String url       = preUrl.substring(0, lastIndex) + relativeUrl;

        section.getTopics().add(new Topic(imgSrc, imgDetailUrl, content, url));

        return section;
    }
}
