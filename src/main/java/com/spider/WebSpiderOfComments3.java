package com.spider;

import com.config.SpringConfig;
import com.dao.CommentDao;
import com.pojo.Comment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * 将评论写到c.txt中，以生成词云
 */
public class WebSpiderOfComments3 {
    public static void main(String[] args) throws IOException {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        CommentDao commentDao = ctx.getBean(CommentDao.class);

        String startWith = "https://movie.douban.com/subject/1292052/comments?";
        String endWith = "limit=20&status=P&sort=new_score";
        String part1 = "start=";
        String part2 = "&";
        Elements comments;
        ArrayList<String> filteredComments = new ArrayList<String>();
        Writer writer = new PrintWriter("c.txt");
        for (int i = 0; i < 10; i++) {
            String between = (i == 0 ? "" : part1 + (i * 20) + part2);
            //评论区每一页的URL
            String url = startWith + between + endWith;
            //获取评论区每页的源码
            Document document = Jsoup.connect(url).get();
            //获取每页评论区的所有评论
            comments = document.select("span.short");
            //遍历comments，将所有评论添加到过滤后的容器中
            for (Element comment : comments) {
                String filteredComment = comment.text();
                filteredComments.add(filteredComment);
            }
        }
        for (String filteredComment : filteredComments) {
            writer.write("评论: " + filteredComment + "\n");
            commentDao.addComment(new Comment(filteredComment));
        }
        writer.flush();
        writer.close();
    }
}
