package com.example.springbootv2.controller.admin;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.controller.BaseController;
import com.example.springbootv2.service.attach.AttachService;
import com.example.springbootv2.utils.TaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin/attach")
public class AttachController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);
    private static final String CLASSPATH = TaleUtils.getUploadFilePath();
}
