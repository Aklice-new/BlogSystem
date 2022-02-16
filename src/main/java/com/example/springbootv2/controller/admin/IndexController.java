package com.example.springbootv2.controller.admin;

import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.StatisticsDto;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.model.LogMD;
import com.example.springbootv2.service.log.LogService;
import com.example.springbootv2.service.site.SiteService;
import com.example.springbootv2.service.user.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/index","/",""})
    public String index(HttpServletRequest request){
        List<CommentMD> comments = siteService.getComments(5);
        List<ContentMD> contents = siteService.getNewArticles(5);
        StatisticsDto statistics = siteService.getStatistics();
        // 取最新的20条日志
        PageInfo<LogMD> logs = logService.getLogs(1, 5);
        List<LogMD> list = logs.getList();
        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", list);
        request.setAttribute("active","home");
        LOGGER.info("Exit admin index method");
        return "admin/index";
    }
}
