package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE = "comment";
    String FIELD = "content, user_id,created_date,entity_id,entity_type,status";
    String KEY = "id, " + FIELD;

    @Select({"SELECT ",KEY," from ",TABLE," WHERE entity_type= #{entityType} AND entity_id=#{entityId}"})
    List<Comment> getAllComments(@Param("entityType")int entityType,@Param("entityId")int entityId);

    @Update({"UPDATE ",TABLE," SET status = #{status} WHERE entity_id=#{entityId} AND entity_type = #{entityType}"})
    int updateCommentStatus(@Param("entityId")int entityId,@Param("entityType")int entityType,@Param("status")int status);

    @Insert({"INSERT INTO ",TABLE,"( ",FIELD,
            " ) VALUES (#{content},#{userId},#{createDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"SELECT count(id) FROM", TABLE," WHERE entity_id=#{entityId} AND entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId")int entityId,@Param("entityType")int entityType);
    @Select({"SELECT ",KEY," from ",TABLE," WHERE id = #{id}"})
    Comment getById(int id);
    @Select({"select count(id) from ", TABLE, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
