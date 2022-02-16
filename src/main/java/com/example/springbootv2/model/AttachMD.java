package com.example.springbootv2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class AttachMD {
    /**文件的id，也是主键*/
    private Integer id;
    /**文件名*/
    private String fname;
    /** 文件类型*/
    private String ftype;
    /** 文件的地址*/
    private String fkey;
    /** 创建人的ID*/
    private Integer authorId;
    /** 文件生成的时间戳*/
    private Integer created;
}
