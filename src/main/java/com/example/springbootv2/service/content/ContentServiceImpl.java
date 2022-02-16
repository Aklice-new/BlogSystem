package com.example.springbootv2.service.content;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dao.CommentsDao;
import com.example.springbootv2.dao.ContentsDao;
import com.example.springbootv2.dao.RelationshipsDao;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.model.RelationShipMD;
import com.example.springbootv2.service.meta.MetaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private ContentsDao contentsDao;
    @Autowired
    private CommentsDao commentsDao;
    @Autowired
    private MetaService metaService;
    @Autowired
    private RelationshipsDao relationshipsDao;
    @Transactional
    @Override
    @CacheEvict(value = {"articleCache","articleCaches"},allEntries = true,beforeInvocation = true)
    public void addArticle(ContentMD contentMD) {
        if(null == contentMD)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        if(StringUtils.isBlank(contentMD.getTitle()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        if(contentMD.getTitle().length() > WebConstant.MAX_TITLE_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        if(StringUtils.isBlank(contentMD.getContent()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        if(contentMD.getContent().length() > WebConstant.MAX_TEXT_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);
        String tags = contentMD.getTags();
        String categories = contentMD.getCategories();
        contentsDao.addArticle(contentMD);

        int cid = contentMD.getCid();
        metaService.addMetas(cid,tags, Type.TAG.getType());
        metaService.addMetas(cid,categories,Type.CATEGORY.getType());
    }

    @Transactional
    @Override
    @CacheEvict(value = {"articleCache","articleCaches"},allEntries = true,beforeInvocation = true)
    public void deleteArticleById(Integer cid) {
        //删除文章还要删除文章的一些评论，还要删除标签和分类关联
        if(cid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        contentsDao.deleteArticleById(cid);
        List<CommentMD>commentMDList = commentsDao.getCommentsById(cid);
        if(commentMDList != null && commentMDList.size() > 0){
            commentMDList.forEach(commentMD -> {
                commentsDao.deleteComment(commentMD.getCoid());
            });
        }
        List<RelationShipMD> relationShipMDs = relationshipsDao.getRelationShipByCid(cid);
        if(relationShipMDs != null && relationShipMDs.size() > 0){
            relationshipsDao.deleteRelationShipByCid(cid);
        }
    }
    @Transactional
    @Override
    @CacheEvict(value = {"articleCache","articleCaches","siteCache"},allEntries = true,beforeInvocation = true)
    public void updateArticleById(ContentMD contentMD) {
        String tags = contentMD.getTags();
        String categories = contentMD.getCategories();

        contentsDao.updateArticle(contentMD);
        int cid = contentMD.getCid();
        relationshipsDao.deleteRelationShipByCid(cid);
        metaService.addMetas(cid,tags,Type.TAG.getType());
        metaService.addMetas(cid,categories,Type.CATEGORY.getType());

    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {

    }

    @Override
    @CacheEvict(value = {"articleCache","articleCaches","siteCache"},allEntries = true,beforeInvocation = true)
    public void updateContent(ContentMD content) {
        if(content == null || content.getCid() == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        contentsDao.updateArticle(content);
    }

    @Override
    @Cacheable(value = {"articleCache","articleCaches"},key = "'articleById_' + #p0")
    public ContentMD getArticleById(Integer cid) {
        if(cid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return contentsDao.getArticleById(cid);
    }

    @Override
    @Cacheable(value = {"articleCache","articleCaches"},key = "'articleByCond_' + #p0")
    public PageInfo<ContentMD> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize) {
        if(contentCond == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum,pageSize);
        List<ContentMD>contentMDList = contentsDao.getArticlesByCond(contentCond);
        PageInfo<ContentMD>pageInfo = new PageInfo<>(contentMDList);
        return pageInfo;
    }

    @Override
    @Cacheable(value = {"articleCache","articleCaches"},key = "'articleByRecent_' + #p0")
    public PageInfo<ContentMD> getRecentlyArticle(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContentMD>contentMDList = contentsDao.getRecentlyArticle();
        PageInfo<ContentMD>pageInfo = new PageInfo<>(contentMDList);
        return pageInfo;
    }

    @Override
    public PageInfo<ContentMD> searchArticle(String param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContentMD>contentMDList = contentsDao.searchArticle(param);
        PageInfo<ContentMD>pageInfo = new PageInfo<>(contentMDList);
        return pageInfo;
    }
}
