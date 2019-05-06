package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.FeedDao;
import com.nowcoder.wenda.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jhc on 2019/5/3
 */

@Service
public class FeedService {

    @Autowired
    FeedDao feedDao;

    public boolean addFeed(Feed feed){
        int re = feedDao.addFeed(feed);
        return re>0;
    }

    public List<Feed> selectUserFeeds(int maxId, List<Integer> userIds,int count){
        return feedDao.selectUserFeeds(maxId,userIds,count);
    }

    public Feed getFeedById(int id){
        return  feedDao.getFeedById(id);
    }
}
