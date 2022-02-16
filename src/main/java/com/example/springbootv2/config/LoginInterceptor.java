package com.example.springbootv2.config;

import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.model.OptionMD;
import com.example.springbootv2.model.UserMD;
import com.example.springbootv2.service.meta.MetaService;
import com.example.springbootv2.service.option.OptionService;
import com.example.springbootv2.service.user.UserService;
import com.example.springbootv2.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserService userService;
    @Autowired
    private Commons commons;
    @Autowired
    private AdminCommons adminCommons;
    @Autowired
    private MetaService metaService;

    private MapCache cache = MapCache.single();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        UserMD user = TaleUtils.getLoginUser(request);
        if(user == null) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (uid != null) {
                user = userService.getUserInfoById(uid);
                request.getSession().setAttribute(WebConstant.LOGIN_SESSION_KEY, user);
            }
        }
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user
                && !uri.startsWith("/admin/css") && !uri.startsWith("/admin/images")
                && !uri.startsWith("/admin/js") && !uri.startsWith("/admin/plugins")) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        //这里是admin登录的拦截器


        // 多敏感的数据接口都很容易被恶意访问和调用，这种情况下为了避免接口被跨页面攻击
        // （以下统称：Cross-site request forgery（CSRF））
        // ，都会在请求接口出增加临时token，避免接口被恶意调用
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Type.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        OptionMD ov = optionService.getOptionByName("site_record");
        List<MetaDto> categories = metaService.getMetaList(Type.CATEGORY.getType(), null, WebConstant.MAX_POSTS);
        List<MetaDto> tags = metaService.getMetaList(Type.TAG.getType(), null, WebConstant.MAX_POSTS);
//        Map<String,String> options = optionService.getOptions().stream().collect(Collectors.toMap(OptionMD::getName,OptionMD::getValue));
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
        httpServletRequest.setAttribute("categories",categories);
        httpServletRequest.setAttribute("tags",tags);
        HandlerInterceptor.super.postHandle(httpServletRequest, response, handler, modelAndView);
    }
}
