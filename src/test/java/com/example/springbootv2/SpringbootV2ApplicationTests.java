package com.example.springbootv2;

import com.example.springbootv2.config.LoginInterceptor;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dao.*;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.model.AttachMD;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.service.comment.CommentService;
import com.example.springbootv2.service.content.ContentService;
import com.example.springbootv2.service.meta.MetaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.swing.plaf.IconUIResource;
import java.util.List;

@SpringBootTest
@Slf4j
class SpringbootV2ApplicationTests {

    @Autowired
    MetaService metaService;
    @Autowired
    ContentService contentService;
    @Autowired
    ContentsDao contentsDao;
    @Autowired
    CommentService commentService;
    @Test
    void contextLoads() {
        List<MetaDto> tags = metaService.getMetaList(Type.TAG.getType(), null, WebConstant.MAX_POSTS);
        tags.forEach(k -> {
            System.out.println(k.getName());
        });
    }

}
