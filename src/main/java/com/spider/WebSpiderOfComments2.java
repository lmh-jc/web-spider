package com.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * 调试用
 */
public class WebSpiderOfComments2 {
    public static void main(String[] args) throws IOException {
        String url = "https://movie.douban.com/subject/1292052/comments?start=180&limit=20&status=P&sort=new_score";
        Document document = Jsoup.connect(url).get();
//        System.out.println(document);
        Elements elements = document.select("h3:not(:has(span[class^=allstar]))");
        elements.append("<span class=\"allstar-non\" title=\"此人未评分\"></span>");
        System.out.println(elements);
    }
}
