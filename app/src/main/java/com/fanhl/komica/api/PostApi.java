package com.fanhl.komica.api;

import com.fanhl.komica.model.Post;
import com.fanhl.komica.model.Reply;
import com.fanhl.komica.model.Topic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by fanhl on 15/10/24.
 */
public class PostApi {
    public static Post getPost(Topic topic) {
        Document document = null;
        try {
            document = Jsoup.connect(topic.getDetailUrl()).timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document == null) {
            return null;
        }

        Post post = new Post();

        Element  contentForm = document.select("form[action=index.php]").get(1);
        Elements elements    = contentForm.select("a[class=del]");//取得返信节点
        for (Element element : elements) {
            String[] imgSrcs = leftAHrefImgUrl(element);

            String imgSrc = imgSrcs != null ? imgSrcs[0] : null;
            String imgDetailUrl = imgSrcs != null ? imgSrcs[1] : null;

            String content = rightBlockquoteContent(element);

            post.getReplies().add(new Reply(imgSrc, imgDetailUrl, content));
        }

        return post;
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
            if (prevElement == null) {
                return null;
            }
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
