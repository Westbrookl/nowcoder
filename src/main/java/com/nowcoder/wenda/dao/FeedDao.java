package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jhc on 2019/5/3
 */
@Mapper
public interface FeedDao {
     String TABLE_NAME = "feed";
     String FILED = "type,user_id,create_date,data";
     String KEY = "id," + FILED;
    @Insert({"INSERT INTO ",TABLE_NAME," ( ",FILED," ) VALUES ( #{type},#{userId},#{createDate},#{data} )"})
    int addFeed(Feed feed);

    @Select({"SELECT ",KEY," FROM ",TABLE_NAME," WHERE id= #{id}"})
    Feed getFeedById(int id);

    List<Feed> selectUserFeeds(@Param("maxId")int maxId,
                               @Param("userIds")List<Integer> userIds,
                               @Param("count")int count);
}
