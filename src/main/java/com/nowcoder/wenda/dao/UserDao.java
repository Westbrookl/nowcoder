package com.nowcoder.wenda.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.nowcoder.wenda.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.security.Key;

/**
 * @author jhc on 2019/4/20
 */
@Mapper
public interface UserDao {
     String TABLE_NAME = "user";
     String KEY = "name,password,salt,head_url";
     String FIELD = "id,"+KEY;
    @Select({"SELECT ",FIELD," FROM ",TABLE_NAME,"WHERE name=#{username}"})
    User getUserByName(@Param("username")String username);

    @Insert({"INSERT INTO ",TABLE_NAME," (", KEY," ) VALUES (#{name},#{password},#{salt},#{headUrl} )"})
    void addUser(User user);
    @Select({"SELECT ",FIELD," FROM ",TABLE_NAME," WHERE id = #{id}"})
    User getUserById(int id);
}
