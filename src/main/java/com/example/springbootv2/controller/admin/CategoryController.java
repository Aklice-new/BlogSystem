package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.service.meta.MetaService;
import com.example.springbootv2.utils.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {
    public static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private MetaService metaService;

    @GetMapping("")
    public String index(HttpServletRequest request){
        List<MetaDto> tags = metaService.getMetaList(Type.TAG.getType(), null, WebConstant.MAX_POSTS);
        List<MetaDto> categories = metaService.getMetaList(Type.CATEGORY.getType(), null, WebConstant.MAX_POSTS);
        request.setAttribute("categories",categories);
        request.setAttribute("tags",tags);
        request.setAttribute("active","category");
        return "admin/category";
    }

    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(
            @RequestParam(name = "cname",required = true)
            String cname,
            @RequestParam(name = "mid",required = false)
            Integer mid
    ){
        try{
            metaService.saveMeta(Type.CATEGORY.getType(), cname,mid);
        }catch (Exception e){
            e.printStackTrace();
            String msg = "Save category failed.";
            if(e instanceof BusinessException){
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            LOGGER.error(msg,e);
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(value = "mid",required = true)
            Integer mid
    ){
        try{
            metaService.deleteMetaById(mid);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }
}
