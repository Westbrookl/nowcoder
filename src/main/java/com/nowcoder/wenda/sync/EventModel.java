package com.nowcoder.wenda.sync;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jhc on 2019/4/29
 */
public class EventModel {
    private int actorId;
    private int entityType;
    private int entityId;
    private int ownerId;
    private EventType type;
    private Map<String, String> map = new HashMap<String, String>();//map是为了针对于不同的情形添加不同的内容。

    public EventModel() {

    }

    public EventModel(EventType type) {
        this.type = type;
    }
    public String getExt(String key){
        return map.get(key);
    }

    public EventModel setExt(String key,String value){
        map.put(key,value);
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public EventModel setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public EventModel setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }
}
