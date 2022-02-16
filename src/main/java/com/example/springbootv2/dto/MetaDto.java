package com.example.springbootv2.dto;

import lombok.Data;

@Data
public class MetaDto {
    /**
     * 项目主键
     */
    private Integer mid;

    /**
     * 名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 对应的文章类型
     */
    private String contentType;

    /**
     * 选项描述
     */
    private String description;

    /**
     * 项目排序
     */
    private Integer sort;
    /**
     * 父级的项目id
     */
    private Integer parent;
    /**
     * 计数器
     */
    private int count;
}
