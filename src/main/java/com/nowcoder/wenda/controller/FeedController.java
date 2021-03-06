package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.Feed;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.service.FeedService;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jhc on 2019/5/3
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(value = "/pullFeeds",method = RequestMethod.GET)
    public String pullFeeds(Model model){
        int localUserId = hostHolder.get() == null?0 :hostHolder.get().getId();
        List<Integer> followees = new ArrayList<>();
        if(localUserId != 0){
            followees = followService.getAllFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.selectUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";

    }
    @RequestMapping(value = "/pushFeeds",method = RequestMethod.GET)
    public String pushFeeds(Model model){
        int localUserId = hostHolder.get() == null ? 0:hostHolder.get().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10);
        List<Feed> feeds = new ArrayList<Feed>();
        for(String feedId:feedIds){
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if(feed != null){
                feeds.add(feed);
            }

        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
