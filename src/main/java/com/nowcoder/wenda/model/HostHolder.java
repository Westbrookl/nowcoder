package com.nowcoder.wenda.model;

import org.springframework.stereotype.Component;

/**
 * @author jhc on 2019/4/20
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public void set(User user){
        threadLocal.set(user);
    }
    public User get(){
        return threadLocal.get();
    }
    public void clear(){
        threadLocal.remove();
    }
}
