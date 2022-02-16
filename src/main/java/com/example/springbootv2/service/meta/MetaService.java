package com.example.springbootv2.service.meta;

import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.cond.MetaCond;
import com.example.springbootv2.model.MetaMD;

import java.util.List;

public interface MetaService {
    /**
     * 添加项目
     * @param meta
     * @return
     */
    void addMeta(MetaMD meta);

    /**
     * 添加
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);



    /**
     * 批量添加
     * @param cid
     * @param names
     * @param type
     */
    void addMetas(Integer cid, String names, String type);



    /**
     * 添加或者更新
     * @param cid
     * @param name
     * @param type
     */
    void saveOrUpdate(Integer cid, String name, String type);

    /**
     * 删除项目
     * @param mid
     * @return
     */
    void deleteMetaById(Integer mid);

    /**
     * 更新项目
     * @param meta
     * @return
     */
    void updateMeta(MetaMD meta);

    /**
     * 根据编号获取项目
     * @param mid
     * @return
     */
    MetaMD getMetaById(Integer mid);

    /**
     * 获取所有的项目
     * @return
     */
    List<MetaMD> getMetas(MetaCond metaCond);

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     * @param type
     * @param orderby
     * @param limit
     * @return
     */
    List<MetaDto> getMetaList(String type, String orderby, int limit);
}
