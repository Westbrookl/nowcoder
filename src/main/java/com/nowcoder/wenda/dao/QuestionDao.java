package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = "question";
    String KEY = "title,content,user_id,created_date,comment_count";
    String FIELD = "id" + KEY;
    @Insert({"INSERT INTO ",TABLE_NAME," ( ",KEY," ) VALUES ( #{title},#{content},#{userId},#{createDate},#{commentCount})" })
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId")int userId,@Param("limit")int limit);
}
