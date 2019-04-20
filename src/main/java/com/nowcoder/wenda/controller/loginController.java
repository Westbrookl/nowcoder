package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jhc on 2019/4/20
 */
@Controller
public class loginController {
    private static final Logger log = LoggerFactory.getLogger(loginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/reglogin", method = RequestMethod.GET)
    public String loginPage(Model model,@RequestParam(value = "next",required = false)String next) {
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String register(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "rememberme",defaultValue = "false")boolean remember,
                           @RequestParam(value = "next",required = false)String next,

                           HttpServletResponse response) {
        try{
            Map<String,Object> map = userService.regiester(username,password);
            if(map.containsKey("ticket")){
               Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
               cookie.setPath("/");
               if(remember){
                   cookie.setMaxAge(3600*24*5*1000);
               }
               response.addCookie(cookie);
               if(StringUtils.isNotBlank(next)){
                   return "redirect:"+next;
               }else{
                   return "redirect:/";
               }
            }else{
                return "login";
            }

        }catch (Exception e){
            log.error("注册出现异常",e.getMessage());
            return "login";
        }
//        Map<String, Object> map = userService.regiester(username, password);
//        if (map.isEmpty()) {
//            return "redirect:/";
//        } else {
//            return "login";
//        }
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public String loginPage(Model model,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam(value = "next",required = false)String next,
                            @RequestParam(value = "rememberme", defaultValue = "false") boolean remember,
                            HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";

            } else {
                model.addAttribute("msg", map);
                return "login";
            }
        } catch (Exception e) {
            log.error("登录异常", e.getMessage());
            return "login";
        }

    }

    @RequestMapping(value ="/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(value = "/user/profile",method = RequestMethod.GET)
    @ResponseBody
    public String test(){
        return "test";
    }

}
