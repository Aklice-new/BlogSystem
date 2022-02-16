package com.example.springbootv2.service.content;

import com.example.springbootv2.dto.cond.ContentCond;
import com.example.springbootv2.model.ContentMD;
import com.github.pagehelper.PageInfo;

public interface ContentService {
    /**
     * 添加文章
     * @param contentMD
     * @return
     */
    void addArticle(ContentMD contentMD);

    /**
     * 根据编号删除文章
     * @param cid
     * @return
     */
    void deleteArticleById(Integer cid);

    /**
     * 更新文章
     * @param contentMD
     * @return
     */
    void updateArticleById(ContentMD contentMD);

    /**
     * 更新分类
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal, String newCatefory);



    /**
     * 添加文章点击量
     * @param content
     */
    void updateContent(ContentMD content);

    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    ContentMD getArticleById(Integer cid);

    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    PageInfo<ContentMD> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize);

    /**
     * 获取最近的文章（只包含id和title）
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ContentMD> getRecentlyArticle(int pageNum, int pageSize);

    /**
     * 搜索文章
     * @param param
     * @param pageNun
     * @param pageSize
     * @return
     */
    PageInfo<ContentMD> searchArticle(String param, int pageNun, int pageSize);
}
