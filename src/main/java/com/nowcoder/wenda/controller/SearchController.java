package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.SearchService;
import com.nowcoder.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jhc on 2019/5/7
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;
    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    /**
     * 处理的逻辑是：
     * 首先取到要搜索问题的列表
     * 然后根据索引得到的问题，填充成为一个完整的问题
     * 因为内容并不是很全，比如没有创建时间的字段也没有问题提出人的问题
     * 所以通过id得到完整的问题，然后把索引到的内容通过加入自定义的前缀加入到问题当中
     * 还需要加入提出人的相关信息
     * 以及关注的人数
     * @param model
     * @param keyWord
     * @param offset
     * @param count
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam("q") String keyWord,
                         @RequestParam(value = "offset",defaultValue = "0") int offset,
                         @RequestParam(value = "count",defaultValue = "10") int count) {
        try {
            List<Question> questionList = searchService.searchQuestion(keyWord, offset, count, "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<ViewObject>();
            for (Question question : questionList) {
                Question q = questionService.getById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUserById(q.getUserId()));
                vos.add(vo);
            }
            System.out.println(vos.size());
            System.out.println(keyWord);
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyWord);
        } catch (Exception e) {
            logger.error("搜索问题出现错误" + e);
        }
        return "result";
    }
}
