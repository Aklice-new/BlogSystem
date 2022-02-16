package com.example.springbootv2.service.attach;

import com.example.springbootv2.dto.AttAchDto;
import com.example.springbootv2.model.AttachMD;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AttachService {
    /**
     * 添加单个附件信息
     * @param attAchDomain
     * @return
     */
    void addAttAch(AttachMD attAchDomain);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    void batchAddAttAch(List<AttachMD> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    void deleteAttAch(Integer id);

    /**
     * 更新附件信息
     * @param attAchDomain
     * @return
     */
    void updateAttAch(AttachMD attAchDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(Integer id);

    /**
     * 获取所有的附件信息
     * @return
     */
    PageInfo<AttAchDto> getAtts(int pageNum, int pageSize);
}
