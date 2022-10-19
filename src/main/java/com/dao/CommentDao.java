package com.dao;

import com.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;


public interface CommentDao {
    @Insert("insert into tbl_comment(comment) values (#{comment})")
    void addComment(Comment comment);
}
