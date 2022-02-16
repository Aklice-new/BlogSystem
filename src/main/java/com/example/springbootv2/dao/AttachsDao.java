package com.example.springbootv2.dao;


import com.example.springbootv2.dto.AttAchDto;
import com.example.springbootv2.model.AttachMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
public interface AttachsDao {
    /**
     * 添加单个附件信息
     * @param attachMD
     * @return
     */
    int addAttAch(AttachMD attachMD);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    int batchAddAttAch(List<AttachMD> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    int deleteAttAch(int id);

    /**
     * 更新附件信息
     * @param attAchDomain
     * @return
     */
    int updateAttAch(AttachMD attAchDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(@Param("id") int id);

    /**
     * 获取所有的附件信息
     * @return
     */
    List<AttAchDto> getAtts();

    /**
     * 查找附件的数量
     * @return
     */
    Long getAttsCount();
}
