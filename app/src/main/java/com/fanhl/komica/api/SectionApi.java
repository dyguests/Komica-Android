package com.fanhl.komica.api;

import android.support.annotation.NonNull;

import com.fanhl.komica.common.C;
import com.fanhl.komica.dummy.SectionDummy;
import com.fanhl.komica.model.BBSMenuItem;
import com.fanhl.komica.model.Section;
import com.fanhl.komica.model.Topic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 版块
 * Created by fanhl on 15/10/24.
 */
public class SectionApi {
    /**
     * 返回版块内容
     *
     * @param bbsMenuItem
     * @return
     */
    public static Section getTopics(BBSMenuItem bbsMenuItem) {
        if (C.DUMMY) {
            return SectionDummy.getTopics(bbsMenuItem);
        }

        Document document = null;
        try {
            document = Jsoup.connect(bbsMenuItem.getUrl()).timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document == null) {
            return null;
        }

        Section section = new Section(bbsMenuItem.getCategory(), bbsMenuItem.getName(), bbsMenuItem.getUrl());

//        Element addForm = document.select("form[action=index.php]").get(0);

        Element  contentForm = document.select("form[action=index.php]").get(1);
        Elements elements    = contentForm.select("a[href^=index.php?res=]");//取得返信节点
        for (Element element : elements) {
            String[] imgSrcs = leftAHrefImgUrl(element);

            String imgSrc = imgSrcs != null ? imgSrcs[0] : null;
            String imgDetailUrl = imgSrcs != null ? imgSrcs[1] : null;

            String content = rightBlockquoteContent(element);

            String detailUrl = getDetailUrl(section, element);

            section.getTopics().add(new Topic(imgSrc, imgDetailUrl, content, detailUrl));
        }

        return section;
    }

    @NonNull
    private static String getDetailUrl(Section section, Element element) {
        String relativeUrl = element.attr("href");
        String preUrl = section.getUrl();
        int lastIndex = preUrl.lastIndexOf("/");
        return preUrl.substring(0, lastIndex + 1) + relativeUrl;
    }

    /**
     * 取得返回按钮左边的档案图片
     * 即同一档案的首张图
     *
     * @param element
     * @return
     */
    private static String[] leftAHrefImgUrl(Element element) {
        int maxLeftTimes = 5;

        Element prevElement = element;
        for (int i = 0; i < maxLeftTimes; i++) {
            prevElement = prevElement.previousElementSibling();
            Elements imgElements = prevElement.select("img[src]");
            if (imgElements.size() > 0) {
                Element imgElement = imgElements.get(0);
                String imgUrl = imgElement.attr("src");
                String imgDetailUrl = prevElement.attr("href");
                return new String[]{imgUrl, imgDetailUrl};
            }
        }
        return null;
    }

    /**
     * 取得返回按钮右边的档案内容文本
     *
     * @param element
     * @return
     */
    private static String rightBlockquoteContent(Element element) {
        int maxRightTimes = 5;

        Element nextElement = element;
        for (int i = 0; i < maxRightTimes; i++) {
            nextElement = nextElement.nextElementSibling();
            Elements imgElements = nextElement.select("blockquote");
            if (imgElements.size() > 0) {
                Element imgElement = imgElements.get(0);
                String text = imgElement.html();
                return text.replaceAll("<br ?/?>|<p>", "\n");
            }
        }
        return null;
    }
}
