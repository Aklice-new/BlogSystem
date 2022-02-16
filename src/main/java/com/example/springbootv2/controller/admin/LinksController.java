package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.dto.cond.MetaCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.MetaMD;
import com.example.springbootv2.service.meta.MetaService;
import com.example.springbootv2.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/links")
public class LinksController extends BaseController {
    @Autowired
    private MetaService metaService;

    @GetMapping("")
    public String index(HttpServletRequest request){
        MetaCond metaCond = new MetaCond();
        metaCond.setType(Type.LINK.getType());
        List<MetaMD>metas = metaService.getMetas(metaCond);
        request.setAttribute("links",metas);
        request.setAttribute("active","links");
        return "admin/links";
    }
    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(
            @RequestParam(value = "title",required = true)
            String title,
            @RequestParam(value = "url",required = true)
            String url,
            @RequestParam(value = "logo",required = false)
            String logo,
            @RequestParam(value = "mid",required = false)
            Integer mid,
            @RequestParam(value = "sort",required = false)
            int sort
    ){
        try{
            MetaMD meta = new MetaMD();
            meta.setName(title);
            meta.setSlug(url);
            meta.setMid(mid);
            meta.setSort(sort);
            meta.setDescription(logo);
            meta.setType(Type.LINK.getType());
            if(mid == null) {
                metaService.addMeta(meta);
            }else{
                metaService.updateMeta(meta);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.ADD_META_FAIL);
        }
        return APIResponse.success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(value = "mid",required = true)
            int mid
    ){
        try{
            metaService.deleteMetaById(mid);
        }catch (Exception e){
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.DELETE_META_FAIL);
        }
        return APIResponse.success();
    }
}
