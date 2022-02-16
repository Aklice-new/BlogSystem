package com.example.springbootv2.dao;

import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.model.CommentMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CommentsDao {
    /**
     * 添加评论
     * @param commentMD  评论
     * @return true false;
     */
    int addComment(CommentMD commentMD);

    /**
     * 删除评论
     * @param coid  评论的id
     * @return
     */
    int deleteComment(@Param("coid")Integer coid);

    /**
     * 更新评论的状态
     * @param coid  评论的id
     * @param status    更新评论后的状态
     * @return
     */
    int updateComment(@Param("coid")Integer coid,@Param("status")String status);

    /**
     * 获取单条评论
     * @param coid 评论的id
     * @return
     */
    CommentMD getCommentById(@Param("coid")Integer coid);

    /**
     * 获取与文章号cid相关的所有评论
     * @param cid
     * @return
     */
    List<CommentMD> getCommentsById(@Param("cid") Integer cid);

    /**
     * 根据条件获取相关评论
     * @param commentCond 评论的条件
     * @return
     */
    List<CommentMD> getCommentsByCond(CommentCond commentCond);

    /**
     * 获取评论的总条数
     * @return
     */
    Long getCommentsCount();
}
