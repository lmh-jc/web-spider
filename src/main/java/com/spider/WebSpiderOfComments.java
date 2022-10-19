package com.spider;

import com.config.SpringConfig;
import com.dao.CommentDao;
import com.pojo.Comment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * 将《肖申克的救赎》影评信息写到b.txt中
 */
public class WebSpiderOfComments {

    public static void main(String[] args) throws IOException {



        String startWith = "https://movie.douban.com/subject/1292052/comments?";

        String endWith = "limit=20&status=P&sort=new_score";

        String part1 = "start=";

        String part2 = "&";

        //存储过滤前的评论相关信息
        Elements nicknames;
        Elements scores;
        Elements comments;
        Elements commentTimes;

        //存储过滤后的评论相关信息
        ArrayList<String> filteredNicknames = new ArrayList<String>();
        ArrayList<String> filteredScores = new ArrayList<String>();
        ArrayList<String> filteredComments = new ArrayList<String>();
        ArrayList<String> filteredCommentTimes = new ArrayList<String>();

        Writer writer = new PrintWriter("b.txt");

        for (int i = 0; i < 10; i++) {

            String between = (i == 0 ? "" : part1 + (i * 20) + part2);

            //评论区每一页的URL
            String url = startWith + between + endWith;

            //获取评论区每页的源码
            Document document = Jsoup.connect(url).get();

            //给没有评分的人加上标签:<span class="allstar-non" title="此人未评分"></span>
            Elements elements = document.select("h3:not(:has(span[class^=allstar]))");
            elements.append("<span class=\"allstar-non\" title=\"此人未评分\"></span>");

            //获取每页评论区中的所有昵称
            nicknames = document.select("a[title]");

            //获取每页评论区中的所有评分
            scores = document.select("span[class^=allstar]");

            //获取每页评论区的所有评论
            comments = document.select("span.short");
            
            ////获取每页评论区的所有评论的评论时间
            commentTimes = document.select("span[class=comment-time]");

            //遍历nicknames，将所有昵称添加到过滤后的容器中
            for (Element nickname : nicknames) {
                String filteredNickname = nickname.attr("title");
                filteredNicknames.add(filteredNickname);
            }

            //遍历scores，将所有评分添加到过滤后的容器中
            for (Element score : scores) {
                String filteredScore = score.attr("class") + "  " + score.attr("title");
                filteredScores.add(filteredScore);
            }

            //遍历comments，将所有评论添加到过滤后的容器中
            for (Element comment : comments) {
                String filteredComment = comment.text();
                filteredComments.add(filteredComment);
            }
            
            //遍历commentTimes，将所有评论的评论时间添加到过滤后的容器中
            for (Element commentTime : commentTimes) {
                String filteredCommentTime = commentTime.attr("title");
                filteredCommentTimes.add(filteredCommentTime);
            }
        }

        for (int i = 0;i < filteredNicknames.size();i++){
            writer.write("\n昵称: " + filteredNicknames.get(i) + "\n");
            writer.write("推荐程度: " + filteredScores.get(i) + "\n");
            writer.write("评论: " + filteredComments.get(i) + "\n");
            writer.write("评论时间: " + filteredCommentTimes.get(i) + "\n");

        }
        writer.flush();
        writer.close();
    }
}
