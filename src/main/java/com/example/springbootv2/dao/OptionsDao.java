package com.example.springbootv2.dao;

import com.example.springbootv2.model.OptionMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OptionsDao {
    /**
     * 删除网站配置
     * @param name
     * @return
     */
    int deleteOptionByName(@Param("name") String name);

    /**
     * 更新网站配置
     * @param option
     * @return
     */
    int updateOptionByName(OptionMD option);

    /***
     * 根据名称获取网站配置
     * @param name
     * @return
     */
    OptionMD getOptionByName(@Param("name") String name);

    /**
     * 获取全部网站配置
     * @return
     */
    List<OptionMD> getOptions();
}
