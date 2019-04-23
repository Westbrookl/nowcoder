package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jhc on 2019/4/17
 */
@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    private List<ViewObject> getQuestion(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question q : questions) {
            ViewObject obj = new ViewObject();
            obj.set("question", q);
            obj.set("user", userService.getUserById(q.getUserId()));
            vos.add(obj);
        }
        return vos;
    }

    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("vos",getQuestion(0,0,10));
        return "index";
    }
}
