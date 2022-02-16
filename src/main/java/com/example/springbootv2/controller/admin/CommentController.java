package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.service.comment.CommentService;
import com.example.springbootv2.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/comments")
public class CommentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("")
    public String index(
            @RequestParam(value = "page",required = false,defaultValue = "1")
                    int page,
            @RequestParam(value = "limit",required = false,defaultValue = "20")
                    int limit,
            HttpServletRequest request){
        PageInfo<CommentMD> comments = commentService.getCommentsByCond(new CommentCond(),page,limit);
        request.setAttribute("comments",comments);
        request.setAttribute("active","comments");
        return "admin/comment_list";
    }

    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(value = "coid",required = true)
                int coid
    ){
        try{
            CommentMD comment = commentService.getCommentById(coid);
            if(comment == null)
                throw BusinessException.withErrorCode(ErrorConstant.Comment.COMMENT_NOT_EXIST);
            commentService.deleteComment(coid);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }
    @PostMapping("/status")
    @ResponseBody
    public APIResponse status(
            @RequestParam(value = "coid",required = true)
                    int coid,
            @RequestParam(value = "status",required = true)
                    String status
    ){
        try{
            CommentMD comment = commentService.getCommentById(coid);
            if(comment == null)
                return APIResponse.fail("delete fail.");
            commentService.updateCommentStatus(coid,status);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }
}
