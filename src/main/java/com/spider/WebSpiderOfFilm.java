package com.spider;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * 将影片信息写到a.txt中
 */
public class WebSpiderOfFilm {

    public static void main(String[] args) throws IOException{
        //排行榜网址前缀
        String startWith = "https://movie.douban.com/top250?start=";

        //排行榜网址后缀
        String endWith = "&filter=";

        //影片链接前缀
        String prefix = "https://movie.douban.com/subject";

        //存储过滤前的链接和影片相关信息
        Elements links;
        Elements texts;
        Elements stars;

        //存储过滤后的链接和影片相关信息
        Elements filteredLinks = new Elements();
        Elements filteredTexts = new Elements();
        ArrayList<String> filteredStars = new ArrayList<String>();

        Writer writer = new PrintWriter("a.txt");

        for (int i = 0; i < 10; i++) {
            //排行榜每一页的URL
            String url = startWith + (i * 25) + endWith;

            //获取排行榜每页的源码
            Document document = Jsoup.connect(url).get();

            //给没有引言的影片加上标签:<p>该影片没有引言</p>
            Elements elements = document.select("div.item:not(:has(p.quote))");
            elements.append("<p>该影片没有引言</p>");

            //获取每页排行榜中的所有链接
            links = document.select("a[href]");

            //获取每页排行榜中的所有<p>
            texts = document.getElementsByTag("p");

            //获取每页电影的评分
            stars = document.select("div.star");

            //遍历links，将所有影片链接添加到过滤后的容器中
            for (Iterator<Element> iterator = links.iterator(); iterator.hasNext(); ) {
                Element link = iterator.next();
                if (link.attr("href").startsWith(prefix)) {
                    link = iterator.next();
                    filteredLinks.add(link);
                }
            }

            //遍历texts，将所有影片相关信息添加到过滤后的容器中
            for (Iterator<Element> iterator = texts.iterator(); iterator.hasNext(); ) {
                Element text1 = iterator.next();
                if (text1.text().startsWith("导演")) {
                    filteredTexts.add(text1);
                    filteredTexts.add(iterator.next());
                }
            }

            //遍历stars，将所有影片评分添加到过滤后的容器中
            for (Element star : stars) {
                filteredStars.add(star.text());
            }
        }

        for (int i = 0;i < filteredLinks.size();i++){
            writer.write("\n影片链接: " + filteredLinks.get(i).attr("href") + "\n");
            writer.write("影片名(别名): " + filteredLinks.get(i).text() + "\n");
            writer.write(filteredTexts.get(2 * i).text() + "\n");
            writer.write("引言: " + filteredTexts.get(2 * i + 1).text() + "\n");
            writer.write("评分: " + filteredStars.get(i) + "\n");
        }
        writer.flush();
        writer.close();
    }
}

