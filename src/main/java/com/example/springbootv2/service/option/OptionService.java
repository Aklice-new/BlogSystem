package com.example.springbootv2.service.option;

import com.example.springbootv2.model.OptionMD;

import java.util.List;
import java.util.Map;

public interface OptionService {
    /**
     * 删除网站配置
     * @param name
     * @return
     */
    void deleteOptionByName(String name);

    /**
     * 更新网站配置
     * @param name
     * @param value
     * @return
     */
    void updateOptionByName(String name, String value);

    /**
     * 保存网站配置
     * @param options
     */
    void saveOptions(Map<String, String> options);
    /***
     * 根据名称获取网站配置
     * @param name
     * @return
     */
    OptionMD getOptionByName(String name);

    /**
     * 获取全部网站配置
     * @return
     */
    List<OptionMD> getOptions();
}
