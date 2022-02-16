package com.example.springbootv2.controller;

import com.example.springbootv2.model.UserMD;
import com.example.springbootv2.service.content.ContentService;
import com.example.springbootv2.service.meta.MetaService;
import com.example.springbootv2.service.site.SiteService;
import com.example.springbootv2.utils.MapCache;
import com.example.springbootv2.utils.TaleUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private MetaService metaService;
    @Autowired
    private SiteService siteService;

    protected MapCache cache = MapCache.single();

    public UserMD user(HttpServletRequest request){
        return TaleUtils.getLoginUser(request);
    }
    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }
    public String join(String [] arr){
        StringBuilder sb = new StringBuilder();
        if(arr == null || arr.length == 0) return null;
        sb.append(arr[0]);
        for(int i = 1;i < arr.length;i ++){
            sb.append(',').append(arr[i]);
        }
        return sb.toString();
    }


}
