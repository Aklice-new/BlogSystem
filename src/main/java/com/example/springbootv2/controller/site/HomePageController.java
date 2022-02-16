package com.example.springbootv2.controller.site;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.service.comment.CommentService;
import com.example.springbootv2.service.content.ContentService;
import com.example.springbootv2.utils.APIResponse;
import com.example.springbootv2.utils.IPKit;
import com.example.springbootv2.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class HomePageController extends BaseController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @GetMapping(value={"/blog","/blog/"})
    public String blog(HttpServletRequest request,
                       @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                       @RequestParam(value = "limit",required = false,defaultValue = "5") int limit){
        request.setAttribute("articles",contentService.getArticlesByCond(new ContentCond(),page,limit));
        request.setAttribute("comments",commentService.getCommentsByCond(new CommentCond(),1,5));
        return "site/blog";
    }

    @GetMapping("/article/{cid}")
    public String articleCid(
            HttpServletRequest request,
            @PathVariable(value = "cid",required = true) int cid
           ){ //
//        System.out.println("Here!");
        ContentMD content = contentService.getArticleById(cid);
        CommentCond cond = new CommentCond();
        cond.setCid(cid);
        cond.setStatus(Type.COMMENTS_PASSED_STATUS.getType());
        PageInfo<CommentMD> comments = commentService.getCommentsByCond(cond,1,999);
        request.setAttribute("article",content);
        request.setAttribute("comments",comments.getList() != null ? comments : null);
        return "site/article";
    }
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public APIResponse comment(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "cid", required = true) Integer cid,
                               @RequestParam(name = "coid", required = false) Integer coid,
                               @RequestParam(name = "author", required = false) String author,
                               @RequestParam(name = "mail", required = false) String mail,
                               @RequestParam(name = "url", required = false) String url,
                               @RequestParam(name = "text", required = true) String text,
                               @RequestParam(name = "_csrf_token", required = true) String _csrf_token) {
        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return APIResponse.fail("访问失败");
        }

        String token = cache.hget(Type.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return APIResponse.fail("访问失败");
        }
        System.out.println(cid);
        System.out.println(text);
        if (null == cid || StringUtils.isBlank(text)) {
            return APIResponse.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return APIResponse.fail("请输入正确的邮箱格式");
        }

        if (text.length() > 200) {
            return APIResponse.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Type.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentMD comments = new CommentMD();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Type.COMMENTS_FREQUENCY.getType(), val, 1, 60);

            return APIResponse.success();
        } catch (Exception e) {
            throw BusinessException.withErrorCode(ErrorConstant.Comment.ADD_NEW_COMMENT_FAIL);
        }
    }
    @GetMapping("/contact")
    public String contact(){
        return "site/contact";
    }


    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
}
