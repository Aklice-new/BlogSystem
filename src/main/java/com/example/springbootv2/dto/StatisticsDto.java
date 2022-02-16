package com.example.springbootv2.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    /**
     * 文章数
     */
    private Long articles;
    /**
     * 评论数
     */
    private Long comments;
    /**
     * 连接数
     */
    private Long links;
    /**
     * 附件数
     */
    private Long attachs;
}
