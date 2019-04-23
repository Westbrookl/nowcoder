package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = "question";
    String KEY = "title,content,user_id,created_date,comment_count";
    String FIELD = "id, " + KEY;
    @Insert({"INSERT INTO ",TABLE_NAME," ( ",KEY," ) VALUES ( #{title},#{content},#{userId},#{createDate},#{commentCount})" })
    int addQuestion(Question question);

    @Select({"SELECT ",FIELD," FROM ",TABLE_NAME,"    ORDER BY id DESC  LIMIT #{offset},#{limit}"})
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
}
