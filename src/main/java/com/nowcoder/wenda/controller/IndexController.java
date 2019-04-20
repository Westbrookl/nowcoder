package com.nowcoder.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jhc on 2019/4/17
 */
@Controller
public class IndexController {
    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }
}
