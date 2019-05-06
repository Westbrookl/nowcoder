package com.nowcoder.wenda.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author jhc on 2019/5/3
 */
public class Feed {
    private int id;
    private int type;
    private Date createDate;
    private int userId;
    private String data;
    private JSONObject jsonObject = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.jsonObject = JSON.parseObject(data);
    }
    public String get(String key){
        return jsonObject == null ? null : jsonObject.getString(key);
    }
}
