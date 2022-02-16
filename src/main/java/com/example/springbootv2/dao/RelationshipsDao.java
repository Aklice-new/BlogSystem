package com.example.springbootv2.dao;

import com.example.springbootv2.model.RelationShipMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelationshipsDao {
    /**
     * 添加
     * @param relationShip
     * @return
     */
    int addRelationShip(RelationShipMD relationShip);

    /**
     * 根据文章编号和meta编号删除关联
     * @param cid
     * @param mid
     * @return
     */
    int deleteRelationShipById(@Param("cid") Integer cid, @Param("mid") Integer mid);

    /**
     * 根据文章编号删除关联
     * @param cid
     * @return
     */
    int deleteRelationShipByCid(@Param("cid") Integer cid);

    /**
     * 根据meta编号删除关联
     * @param mid
     * @return
     */
    int deleteRelationShipByMid(@Param("mid") Integer mid);

    /**
     * 更新
     * @param relationShip
     * @return
     */
    int updateRelationShip(RelationShipMD relationShip);

    /**
     * 根据文章主键获取关联
     * @param cid
     * @return
     */
    List<RelationShipMD> getRelationShipByCid(@Param("cid") Integer cid);

    /**
     * 根据meta编号获取关联
     * @param mid
     * @return
     */
    List<RelationShipMD> getRelationShipByMid(@Param("mid") Integer mid);

    /**
     * 获取数量
     * @param cid
     * @param mid
     * @return
     */
    Long getCountById(@Param("cid") Integer cid, @Param("mid") Integer mid);
}
