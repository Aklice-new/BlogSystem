package com.example.springbootv2.controller;

import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.service.content.ContentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    @Autowired
    private ContentService contentService;

    //关于index页面的controller
    @GetMapping(value = {"/index","/","","/home"})
    public String index(HttpServletRequest request,
                        @RequestParam(value = "limit",defaultValue = "12")int limit){
        return this.index(request,1,limit);
    }
    @GetMapping(value = "/photo/page/{p}")
    public String index(HttpServletRequest request,
                        @RequestParam(value = "p")int page,
                        @RequestParam(value = "limit",defaultValue = "5",required = false)int limit){
        page = page < 0 || page > WebConstant.MAX_PAGE ? 1 : page;
        ContentCond cond = new ContentCond();
        cond.setType(Type.PHOTO.getType());
        PageInfo<ContentMD>articles = contentService.getArticlesByCond(cond,page,limit);
        request.setAttribute("archives", articles);
        request.setAttribute("active","work");
        return "site/index";
    }
}
