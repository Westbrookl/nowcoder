package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = "login_ticket";
    String KEY = " user_id, expired, status, ticket ";
    String FIELD = "id, " + KEY;

    @Insert({"INSERT INTO",TABLE_NAME," ( ",KEY," ) VALUES( #{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"SELECT ",FIELD," FROM ",TABLE_NAME," WHERE ticket = #{ticket}"})
    LoginTicket getByTicket(String ticket);

    @Update({"UPDATE ",TABLE_NAME, " SET status = #{status} WHERE ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
