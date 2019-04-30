package com.nowcoder.wenda.sync;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jhc on 2019/4/29
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType,List<EventHandler>>();

    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> handlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if(handlerMap != null) {
            for (Map.Entry<String, EventHandler> map : handlerMap.entrySet()) {
                List<EventType> types = map.getValue().getSupportEventType();
                /**
                 *
                 */
                for (EventType type : types) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(map.getValue());
                }
            }
        }
//        System.out.println("111");
//        ExecutorService executor = Executors.newFixedThreadPool(10);
        new Thread(new Run1()).start();

    }
    private class Run1 implements Runnable{
        public void run(){
            while(true){
                String key = RedisKeyUtil.getQueueKey();
//                System.out.println(key);
                List<String> values = jedisAdapter.brpop(0,key);
//                System.out.println(values);
                for(String value : values){
                    if(value.equals(key)){
                        continue;
                    };
                    EventModel model = JSON.parseObject(value,EventModel.class);

                    System.out.println(model.getType());
                    if(!config.containsKey(model.getType())){
                        logger.error("不能识别的事件");
                        continue;
                    }
                    for(EventHandler handler:config.get(model.getType())){
//                        System.out.println("for handle");
                        handler.doHandle(model);
                    }

                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}


//@Service
//public class EventConsumer implements InitializingBean, ApplicationContextAware {
//
//    private Logger logger = LoggerFactory.getLogger(EventConsumer.class);
//    private ApplicationContext applicationContext;
//    @Autowired
//    private JedisAdapter jedisAdapter;
//
//    private Map<EventType, List<EventHandler>> map = new HashMap<EventType,List<EventHandler>>();
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Map<String,EventHandler> bean = applicationContext.getBeansOfType(EventHandler.class);
//        if(bean != null){
//            for(Map.Entry<String,EventHandler> entry:bean.entrySet()){
//                List<EventType> types = entry.getValue().getSupportEventType();
////                for(Map.Entry<EventType,List<EventHandler>> entryHandler:map.entrySet()){
////                    if(entryHandler.)
////                }
//                for(EventType type:types){
//                    if(map.containsKey(type)){
//                        map.get(type).add(entry.getValue());
//                    }else{
//                        map.put(type,new ArrayList<EventHandler>());
//                    }
//                }
//            }
//        }
//        new Thread(new Runnable(){
//            public void  run(){
//                while(true){
//                    String key = RedisKeyUtil.getQueueKey();
//                    List<String> values = jedisAdapter.brpop(0,key);
//                    for(String value:values){
//                        if(value.equals(key)){
//                            continue;
//                        }
//                        EventModel model = JSONObject.parseObject(value,EventModel.class);
//                        if(!map.containsKey(model.getType())){
//                            logger.error("不能识别的事件");
//                            continue;
//                        }
//
//                        for(EventHandler handler:map.get(model.getType())){
//                            handler.doHandle(model);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}
