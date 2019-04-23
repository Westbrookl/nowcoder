package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.QuestionDao;
import com.nowcoder.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author jhc on 2019/4/22
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    /**
     * 添加的问题的具体实现的代码：
     * 当添加成功的时候，数据库返回的是1，成功返回问题id否则返回0
     * 下面是对消息进行过滤
     * @param question
     * @return
     */
    public int addQuestion(Question question){
        //去除数据的html的标签防止注入脚本
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question) > 0 ? question.getId():0;
    }

}
