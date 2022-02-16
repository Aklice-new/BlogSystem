package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.model.OptionMD;
import com.example.springbootv2.service.log.LogService;
import com.example.springbootv2.service.option.OptionService;
import com.example.springbootv2.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {
    @Autowired
    private OptionService optionService;
    @Autowired
    private LogService logService;

    @GetMapping("")
    public String setting(HttpServletRequest request){
        List<OptionMD> optionList = optionService.getOptions();
        Map<String,String> options = new HashMap<>();
        optionList.forEach(option ->{
            options.put(option.getName(),option.getValue());
        });
        request.setAttribute("options",options);
        request.setAttribute("active","setting");
        return "admin/setting";
    }
    @PostMapping("")
    @ResponseBody
    public APIResponse saveSetting(HttpServletRequest request){
        try{
            Map<String,String[]> parameterMap = request.getParameterMap();
            Map<String,String> queries = new HashMap<>();
            parameterMap.forEach((key,value)->{
                queries.put(key,join(value));
            });
            optionService.saveOptions(queries);
            List<OptionMD>options = optionService.getOptions();
//            if(!CollectionUtils.isEmpty(options)){
//                WebConstant.initConfig = options.stream().collect(Collectors.toMap(OptionMD::getName,OptionMD::getValue));
//            }
//            //logService.addLog(LogActions.SYS_SETTING.getAction(), Go);
            return APIResponse.success();
        }catch (Exception e) {
            e.printStackTrace();
            String msg = "保存设置失败";
            return APIResponse.fail(e.getMessage());
        }
    }

}
