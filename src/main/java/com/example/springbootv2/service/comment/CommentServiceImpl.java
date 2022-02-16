package com.example.springbootv2.service.comment;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.dao.CommentsDao;
import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.service.content.ContentService;
import com.example.springbootv2.utils.DateUtils;
import com.example.springbootv2.utils.TaleUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentsDao commentsDao;
    @Autowired
    private ContentService contentService;

    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();
    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * 评论状态：不显示
     */
    private static final String STATUS_BLANK = "not_audit";

    static {
        STATUS_MAP.put("approved",STATUS_NORMAL);
        STATUS_MAP.put("not_audit",STATUS_BLANK);
    }
    @Override
    @Transactional
    @CacheEvict(value = {"commentCache","siteCache"},allEntries = true,beforeInvocation = true)
    public void addComment(CommentMD commentMD) {
        //添加评论的同时也要修改被评论文章的评论数
        String msg = null;
        if(null == commentMD)
            msg = "评论对象为空";
        if(commentMD != null){
            if(StringUtils.isBlank(commentMD.getAuthor()))
                commentMD.setAuthor("unknown");
            if (StringUtils.isNotBlank(commentMD.getMail()) && !TaleUtils.isEmail(commentMD.getMail())) {
                msg = "邮箱格式不正确";
            }
            if(StringUtils.isBlank(commentMD.getContent())){
                msg = "评论为空";
            }
            if(commentMD.getContent().length() < 2 || commentMD.getContent().length() > 2000){
                msg = "评论字数在2-20000范围内";
            }
            if(commentMD.getCid() == null)
                msg = "评论文章不能为空";
            if(msg != null)
                throw BusinessException.withErrorCode(msg);
            ContentMD article = contentService.getArticleById(commentMD.getCid());
            if(article == null)
                throw BusinessException.withErrorCode("该文章不存在");
            commentMD.setOwnerId(article.getAuthorId());
            commentMD.setStatus(STATUS_MAP.get(STATUS_BLANK));
            commentMD.setCreated(DateUtils.getCurrentUnixTime());
            commentsDao.addComment(commentMD);

            ContentMD temp = new ContentMD();
            temp.setCid(article.getCid());
            Integer count = article.getCommentsNum();
            if(count == null)
                count = 0;
            temp.setCommentsNum(count + 1);
            contentService.updateContent(temp);
        }
    }
    @Transactional
    @Override
    @CacheEvict(value = {"commentCache","siteCache"},allEntries = true,beforeInvocation = true)
    public void deleteComment(Integer coid) {
        if(coid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        CommentCond commentCond = new CommentCond();
        commentCond.setParent(coid);
        CommentMD commentMD = commentsDao.getCommentById(coid); //当前的评论
        List<CommentMD> childCommentMDList = commentsDao.getCommentsByCond(commentCond);//获取子评论
        Integer count = 0; // 记录删除的评论数
        //删除子评论
        if(childCommentMDList != null && childCommentMDList.size() > 0){
            for (int i = 0;i < childCommentMDList.size();i ++){
                commentsDao.deleteComment(childCommentMDList.get(i).getCoid());
                count ++;
            }
        }
        //删除当前评论
        commentsDao.deleteComment(coid);
        count ++;

        ContentMD contentMD = contentService.getArticleById(commentMD.getCid());

        if(contentMD != null
                && contentMD.getCommentsNum() != null
                && contentMD.getCommentsNum() != 0){
            contentMD.setCommentsNum(contentMD.getCommentsNum() - count);
            contentService.updateContent(contentMD);
        }

    }

    @Override
    @CacheEvict(value = {"commentCache","siteCache"},allEntries = true,beforeInvocation = true)
    public void updateCommentStatus(Integer coid, String status) {
        if(coid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        commentsDao.updateComment(coid,status);
    }

    @Override
    @Cacheable(value = "commentCache",key = "'commentById_' + #p0")
    public CommentMD getCommentById(Integer coid) {
        if(coid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentsDao.getCommentById(coid);
    }

    @Override
    @Cacheable(value = "commentCache",key = "'commentsById_' + #p0")
    public List<CommentMD> getCommentsByCId(Integer cid) {
        if(cid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentsDao.getCommentsById(cid);
    }

    @Override
    @Cacheable(value = "commentCache",key = "'commentsByCond_' + #p0")
    public PageInfo<CommentMD> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize) {
        if(commentCond == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum,pageSize);
        List<CommentMD>comments = commentsDao.getCommentsByCond(commentCond);
        PageInfo<CommentMD>pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }
}
