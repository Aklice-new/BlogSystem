package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.LogActions;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.dto.cond.MetaCond;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.model.MetaMD;
import com.example.springbootv2.service.content.ContentService;
import com.example.springbootv2.service.log.LogService;
import com.example.springbootv2.service.meta.MetaService;
import com.example.springbootv2.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private LogService logService;


    @GetMapping("")
    public String index(
            HttpServletRequest request,
            @RequestParam(value = "page",required = false,defaultValue = "1")int page,
            @RequestParam(value = "limit",required = false,defaultValue = "5")int limit
    ){
        PageInfo<ContentMD> articles = contentService.getArticlesByCond(new ContentCond(),page,limit);
        request.setAttribute("articles",articles);
        request.setAttribute("active","article");
        return "admin/article_list";
    }

    @GetMapping("/publish")
    public String newArticle(HttpServletRequest request){
        MetaCond metaCond = new MetaCond();
        metaCond.setType(Type.CATEGORY.getType());
        List<MetaMD> metas = metaService.getMetas(metaCond);
        request.setAttribute("categories",metas);
        request.setAttribute("active","publish");
        return "admin/article_edit";
    }
    @PostMapping("/publish")
    @ResponseBody
    public APIResponse publicArticle(
            HttpServletRequest request,
            @RequestParam(name = "title",required = true)
            String title,
            @RequestParam(name = "titlePic",required = false)
            String titlePic,
            @RequestParam(name = "slug",required = false)
            String slug,
            @RequestParam(name="content",required = true)
            String content,
            @RequestParam(name = "type",required = true)
            String type,
            @RequestParam(name="status",required = true)
            String status,
            @RequestParam(name = "tags",required = false)
            String tags,
            @RequestParam(name = "categories",required = false,defaultValue = "默认分类")
            String categories,
            @RequestParam(name = "allowComment",required = true)
            Boolean allowComment
    ){
        ContentMD contentDomain = new ContentMD();
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(type.equals(Type.ARTICLE.getType()) ? tags : null);
        contentDomain.setCategories(type.equals(Type.ARTICLE.getType()) ? categories : null);
        contentDomain.setAllowComment(allowComment ? 1 : 0);

        contentService.addArticle(contentDomain);
        request.setAttribute("active","publish");
        return APIResponse.success();
    }
    @GetMapping(value = "/{cid}")
    public String editArticle(
            @PathVariable Integer cid,
            HttpServletRequest request
    ){
        ContentMD content = contentService.getArticleById(cid);
        request.setAttribute("contents",content);
        MetaCond metaCond = new MetaCond();
        metaCond.setType(Type.CATEGORY.getType());
        List<MetaMD>categories = metaService.getMetas(metaCond);
        request.setAttribute("categories",categories);
        return "admin/article_edit";
    }
    @PostMapping("/modify")
    @ResponseBody
    public APIResponse modifyArticle(
            HttpServletRequest request,
            @RequestParam(name = "cid",required = true)
                    Integer cid,
            @RequestParam(name = "title",required = true)
                    String title,
            @RequestParam(name = "titlePic",required = false)
                    String titlePic,
            @RequestParam(name = "slug",required = false)
                    String slug,
            @RequestParam(name="content",required = true)
                    String content,
            @RequestParam(name = "type",required = true)
                    String type,
            @RequestParam(name="status",required = true)
                    String status,
            @RequestParam(name = "tags",required = false)
                    String tags,
            @RequestParam(name = "categories",required = false,defaultValue = "默认分类")
                    String categories,
            @RequestParam(name = "allowComment",required = true)
                    Boolean allowComment
    ){
        ContentMD contentDomain = new ContentMD();
        contentDomain.setCid(cid);
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(tags);
        contentDomain.setCategories(categories);
        contentDomain.setAllowComment(allowComment ? 1 : 0);

        contentService.updateArticleById(contentDomain);
        return APIResponse.success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse deleteArticle(
            HttpServletRequest request,
            @RequestParam(name = "cid",required = true)
            Integer cid
    ){
        contentService.deleteArticleById(cid);
        logService.addLog(LogActions.DEL_ARTICLE.getAction(),cid + "", request.getRemoteAddr(), this.getUid(request));
        return APIResponse.success();
    }
}
