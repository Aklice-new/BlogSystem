package com.example.springbootv2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentMD {
    /** 评论表的主键*/
    private Integer coid;
    /** 评论的id*/
    private Integer cid;
    /** 评论生成的时间戳*/
    private Integer created;
    /** 评论作者*/
    private String author;
    /** 评论作者id*/
    private Integer authorId;
    /** 被评论作者id*/
    private Integer ownerId;
    /** 评论者的邮箱*/
    private String mail;
    /** */
    private String url;
    /** */
    private String ip;
    /** */
    private String agent;
    /** */
    private String type;
    /** 评论状态*/
    private String status;
    /** 父级评论*/
    private Integer parent;
    /** 评论内容*/
    private String content;
}
