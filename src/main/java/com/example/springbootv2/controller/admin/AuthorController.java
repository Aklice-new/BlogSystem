package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.LogActions;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.UserMD;
import com.example.springbootv2.service.log.LogService;
import com.example.springbootv2.service.user.UserService;
import com.example.springbootv2.utils.APIResponse;
import com.example.springbootv2.utils.IPKit;
import com.example.springbootv2.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/admin")
public class AuthorController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(AuthorController.class);

    @Autowired
    UserService userService;
    @Autowired
    LogService logService;

    @GetMapping(value = {"/login"})
    public String login(){
        return "admin/login";
    }
    @PostMapping(value = "/login")
    @ResponseBody
    public APIResponse toLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "username",required = true) String username,
            @RequestParam(name = "password",required = true)String password,
            @RequestParam(name = "remember_me",required = false)String remember_me
            ){
        String ip = IPKit.getIpAddrByRequest(request);
        Integer error_count = cache.hget("login_error_count",ip);
        try{
            UserMD userInfo = userService.login(username,password);
            System.out.println(userInfo);
            request.getSession().setAttribute(WebConstant.LOGIN_SESSION_KEY,userInfo);
            if(StringUtils.isNotBlank(remember_me)){
                TaleUtils.setCookie(response,userInfo.getUid());
            }
            logService.addLog(LogActions.LOGIN.getAction(),null,request.getRemoteAddr(),userInfo.getUid());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            error_count = error_count == null ? 1 : error_count + 1;
            if(error_count > 3){
                return APIResponse.fail("您输入的密码已经超过三次");
            }
            cache.hset("login_error_count",ip,error_count,10 * 60);
            String msg = "登录失败";
            if(e instanceof BusinessException){
                msg = e.getMessage();
            } else LOGGER.error(msg,e);
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(WebConstant.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConstant.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
