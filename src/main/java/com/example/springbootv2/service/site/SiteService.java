package com.example.springbootv2.service.site;

import com.example.springbootv2.dto.ArchiveDto;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.StatisticsDto;
import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.model.CommentMD;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.model.OptionMD;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SiteService {
    /**
     * 获取评论列表
     * @param limit
     * @return
     */
    List<CommentMD> getComments(int limit);

    /**
     * 获取最新的文章
     * @param limit
     * @return
     */
    List<ContentMD> getNewArticles(int limit);

    /**
     * 获取单条评论
     * @param coid
     * @return
     */
    CommentMD getComment(Integer coid);

    /**
     * 获取 后台统计数据
     * @return
     */
    StatisticsDto getStatistics();

    /**
     * 获取归档列表 - 只是获取日期和数量
     * @param contentCond
     * @return
     */
    List<ArchiveDto> getArchivesSimple(ContentCond contentCond);

    /**
     * 获取归档列表
     * @param contentCond 查询条件（只包含开始时间和结束时间）
     * @return
     */
    List<ArchiveDto> getArchives(ContentCond contentCond);



    /**
     * 获取分类/标签列表
     * @param type
     * @param orderBy
     * @param limit
     * @return
     */
    List<MetaDto> getMetas(String type, String orderBy, int limit);
}
