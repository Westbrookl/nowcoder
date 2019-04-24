package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author jhc on 2019/4/24
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "message";
    String FIELD = " from_id,to_id,content,create_date,has_read,conversation_id";
    String KEY = "id, "+FIELD;
    @Insert({"INSERT INTO ",TABLE_NAME," ( ",FIELD," ) VALUES ( #{fromId},#{toId},#{content},#{createDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"SELECT ",KEY," FROM",TABLE_NAME," WHERE conversation_id = #{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getAllMessageByConversationId(@Param("conversationId")String conversationId,@Param("offset")int offset,
                                                @Param("limit")int limit);

    @Select({"SELECT ",FIELD," , count(id) as id from ( select * from ",TABLE_NAME," WHERE from_id = #{userId} OR to_id=#{userId} ORDER BY id desc) tt GROUP BY conversation_id ORDER BY create_date DESC limit #{offset},#{limit}"})
    List<Message> getAllConversations(@Param("userId")int userId,@Param("offset")int offset,@Param("limit")int limit);


    @Select({"SELECT count(id) from ",TABLE_NAME," where has_read = 0 AND conversation_id = #{conversationId} AND to_id=#{userId}"})
    int getConversationUnread(@Param("userId")int userId,@Param("conversationId")String conversationId);

    @Update({"UPDATE ",TABLE_NAME," set has_read=#{status} WHERE id = #{messageId}"})
    int updateReadStatus(@Param("messageId")int messageId,@Param("status")int status);
}
