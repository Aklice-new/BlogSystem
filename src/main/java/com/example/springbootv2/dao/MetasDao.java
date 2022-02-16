package com.example.springbootv2.dao;

import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.cond.MetaCond;
import com.example.springbootv2.model.MetaMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MetasDao {
    /**
     * 添加项目
     * @param meta
     * @return
     */
    int addMeta(MetaMD meta);

    /**
     * 删除项目
     * @param mid
     * @return
     */
    int deleteMetaById(@Param("mid") Integer mid);

    /**
     * 更新项目
     * @param meta
     * @return
     */
    int updateMeta(MetaMD meta);

    /**
     * 根据编号获取项目
     * @param mid
     * @return
     */
    MetaMD getMetaById(@Param("mid") Integer mid);


    /**
     * 根据条件查询
     * @param metaCond
     * @return
     */
    List<MetaMD> getMetasByCond(MetaCond metaCond);

    /**
     * 根据类型获取meta数量
     * @param type
     * @return
     */
    Long getMetasCountByType(@Param("type") String type);

    /**
     * 根据sql查询
     * @param paraMap
     * @return
     */
    List<MetaDto> selectFromSql(Map<String, Object> paraMap);

}
