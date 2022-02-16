package com.example.springbootv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class MyController {
    @RequestMapping({"/index","/"})
    public String index(){
        return "site/index.html";
    }
    @RequestMapping("/nothing")
    public String nothing(){
        return "redirect:/layui/layui.js";
    }
}
