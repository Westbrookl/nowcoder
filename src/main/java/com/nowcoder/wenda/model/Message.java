package com.nowcoder.wenda.model;

import java.util.Date;

/**
 * @author jhc on 2019/4/24
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private Date createDate;
    private String conversationId;
    private String content;
    private int hasRead;

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getConversationId() {
        if (fromId > toId) {
            return String.valueOf(toId) + String.valueOf(fromId);
        } else {
            return String.valueOf(fromId) + String.valueOf(toId);
        }

    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
