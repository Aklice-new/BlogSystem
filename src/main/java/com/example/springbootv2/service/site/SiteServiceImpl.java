package com.example.springbootv2.service.site;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dao.AttachsDao;
import com.example.springbootv2.dao.CommentsDao;
import com.example.springbootv2.dao.ContentsDao;
import com.example.springbootv2.dao.MetasDao;
import com.example.springbootv2.dto.ArchiveDto;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.StatisticsDto;
import com.example.springbootv2.dto.cond.CommentCond;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SiteServiceImpl implements SiteService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private CommentsDao commentsDao;

    @Autowired
    private ContentsDao contentsDao;

    @Autowired
    private MetasDao metasDao;

    @Autowired
    private AttachsDao attachsDao;

    @Override
    @Cacheable(value = "siteCache",key = "'comments_' +#p0")
    public List<CommentMD> getComments(int limit) {
        LOGGER.debug("Enter recentComments method:limilt={}",limit);
        if(limit < 0 || limit > 10)
            limit = 10;
        PageHelper.startPage(1,limit);
        List<CommentMD>commentMDList = commentsDao.getCommentsByCond(new CommentCond());
        LOGGER.debug("Exit recentComments method");
        return commentMDList;
    }

    @Override
    @Cacheable(value = "siteCache",key = "'newArticles_' +#p0")
    public List<ContentMD> getNewArticles(int limit) {
        LOGGER.debug("Enter recentComments method:limilt={}",limit);
        if(limit < 0 || limit > 10)
            limit = 10;
        PageHelper.startPage(1,limit);
        List<ContentMD>contentMDList = contentsDao.getArticlesByCond(new ContentCond());
        LOGGER.debug("Exit recentComments method");
        return contentMDList;
    }

    @Override
    @Cacheable(value = "siteCache",key = "'comment_' +#p0")
    public CommentMD getComment(Integer coid) {
        LOGGER.debug("Enter comment search by id method");
        if(coid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        CommentMD commentMD = commentsDao.getCommentById(coid);
        LOGGER.debug("Exit comment search by id method");
        return commentMD;
    }

    @Override
    @Cacheable(value = "siteCache",key = "'statistics_' + #p0")
    public StatisticsDto getStatistics() {
        LOGGER.debug("Enter statistics method");
        Long articles = contentsDao.getArticleCount();
        Long comments = commentsDao.getCommentsCount();
        Long links = metasDao.getMetasCountByType(Type.LINK.getType());
        Long atts = attachsDao.getAttsCount();
        StatisticsDto statisticsDto = new StatisticsDto() ;
        statisticsDto.setArticles(articles);
        statisticsDto.setAttachs(atts);
        statisticsDto.setLinks(links);
        statisticsDto.setComments(comments);
        LOGGER.debug("Exit statistic method");
        return statisticsDto;
    }

    @Override
    @Cacheable(value = "siteCache",key = "'archiveSimpl_' +#p0")
    public List<ArchiveDto> getArchivesSimple(ContentCond contentCond) {
        List<ArchiveDto> archiveDtoList = contentsDao.getArchive(contentCond);
        return archiveDtoList;
    }

    @Override
    @Cacheable(value = "siteCache",key = "'archives_' +#p0")
    public List<ArchiveDto> getArchives(ContentCond contentCond) {
        List<ArchiveDto>archiveDtoList = contentsDao.getArchive(contentCond);
        parseArchives(archiveDtoList,contentCond);
        return archiveDtoList;
    }
    private void parseArchives(List<ArchiveDto>archiveDtoList,ContentCond contentCond){
        if(archiveDtoList == null)
            return ;
        archiveDtoList.forEach(var->{
            String date = var.getDate();
            Date sd = DateUtils.dateFormat(date,"yyyy年MM月");
            int start = DateUtils.getUnixTimeByDate(sd);
            int end = DateUtils.getUnixTimeByDate(DateUtils.dateAdd(DateUtils.INTERVAL_MINUTE,sd,1)) - 1;
            ContentCond cond = new ContentCond();
            cond.setStartTime(start);
            cond.setEndTime(end);
            cond.setType(contentCond.getType());
            List<ContentMD>contents = contentsDao.getArticlesByCond(cond);
            var.setArticles(contents);
        });
    }

    @Override
    public List<MetaDto> getMetas(String type, String orderBy, int limit) {
        List<MetaDto> metaDtos = null;
        if(StringUtils.isNotBlank(type)){
            if(StringUtils.isBlank(orderBy))
                orderBy = "count desc,a.mid desc";
            if(limit < 1 || limit < WebConstant.MAX_POSTS)
                limit = 10;
            Map<String,Object>parmp = new HashMap<>();
            parmp.put("type",type);
            parmp.put("order",orderBy);
            parmp.put("limit",limit);
            metaDtos = metasDao.selectFromSql(parmp);
        }
        return metaDtos;
    }
}
