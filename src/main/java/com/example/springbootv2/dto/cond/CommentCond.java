package com.example.springbootv2.dto.cond;

import lombok.Data;

@Data
public class CommentCond {
    /**
     * 状态
     */
    private String status;
    /**
     * 开始时间戳
     */
    private Integer startTime;
    /**
     * 结束时间戳
     */
    private Integer endTime;

    /**
     * 父评论编号
     */
    private Integer parent;
    /**
     * 文章编号
     */
    private Integer cid;
}

