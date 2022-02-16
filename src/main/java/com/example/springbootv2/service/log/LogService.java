package com.example.springbootv2.service.log;

import com.example.springbootv2.model.LogMD;
import com.github.pagehelper.PageInfo;

public interface LogService {
    /**
     * 添加
     * @param action
     * @param data
     * @param ip
     * @param authorId
     */
    void addLog(String action, String data, String ip, Integer authorId);

    /**
     * 删除日志
     * @param id
     * @return
     */
    void deleteLogById(Integer id);

    /**
     * 获取日志
     * @return
     */
    PageInfo<LogMD> getLogs(int pageNum, int pageSize);
}
