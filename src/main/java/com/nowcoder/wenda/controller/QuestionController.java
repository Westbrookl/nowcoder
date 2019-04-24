package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.dao.CommentDao;
import com.nowcoder.wenda.model.*;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author jhc on 2019/4/22
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserService userService;

    /**
     * 添加问题的逻辑为：
     * 首先构建出一个符合数据库的类，然后去创建数据库里面的内容
     * 有两个特殊的地方：一个是当前没有登录的时候，设置一个匿名的账户
     * 第二个：当账户登录的时候，创建问题成功的话，返回的是一个json（code =0）
     * 当创建问题失败的时候，创建的是一个json(1，“msg = ‘失败’”);
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/question/add",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@Param("title")String title,@Param("content")String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCommentCount(0);
            question.setCreateDate(new Date());
            User user = hostHolder.get();
            if(user == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                question.setUserId(user.getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("添加问题错误",e.getMessage());
        }

            return WendaUtil.getJSONString(1,"失败");

    }


    @RequestMapping(value="/question/{qid}",method = RequestMethod.GET)
    public String questionDetail(Model model, @PathVariable("qid")int qid){
        Question question = questionService.getById(qid);
        model.addAttribute("question",question);

        List<Comment> comments = commentDao.getAllComments(EntityType.ENTITY_QUESTION,qid);
        List<ViewObject> viewObjects = new ArrayList<ViewObject>();
        for(Comment c1:comments){
            ViewObject vo = new ViewObject();
            vo.set("comment",c1);
            vo.set("user",userService.getUserById(c1.getUserId()));
            viewObjects.add(vo);
        }
        model.addAttribute("comments",viewObjects);
        return "detail";
    }
}
