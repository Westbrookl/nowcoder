package com.nowcoder.wenda.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jhc on 2019/4/22
 */
public class ViewObject {
    private Map<String,Object>  map = new HashMap<String,Object>();
    public void set(String key,Object value){
        map.put(key,value);
    }
    public Object get(String key){
        return map.get(key);
    }
}
