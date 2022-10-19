package com.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebSpiderOfFilm2 {
    public static void main(String[] args) throws IOException {
        String url = "https://movie.douban.com/top250?start=225&filter=";
        Document document = Jsoup.connect(url).get();
        System.out.println(document);
//        Elements elements = document.select("div.item:not(:has(p.quote))");
//        elements.append("<p>该影片没有引言</p>");
//        System.out.println(elements);
    }
}