package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.CommentDao;
import com.nowcoder.wenda.model.Comment;
import com.nowcoder.wenda.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jhc on 2019/4/23
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    SensitiveService sensitiveService;

    public int addComment(Comment comment){
//        comment.setContent(sensitiveService.filter(comment.getContent()));

        return commentDao.addComment(comment) > 0 ? comment.getId():0;

    }

    public List<Comment> getAllComments(int entityType,int entityId){
        return commentDao.getAllComments(entityType,entityId);
    }

    public int deleteComment(int entityType,int entityId,int status){
        return commentDao.updateCommentStatus(entityId,entityType,status);
    }

    public int getCommentCount(int entityType,int entityId){

        return commentDao.getCommentCount(entityId,entityType);
    }

    public Comment getById(int id){
        return commentDao.getById(id);
    }
}
