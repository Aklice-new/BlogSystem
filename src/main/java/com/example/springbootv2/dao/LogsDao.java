package com.example.springbootv2.dao;

import com.example.springbootv2.model.LogMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogsDao {
    /**
     * 增添日志
     * @param logMd
     * @return
     */
    int addLogMD(LogMD logMd);

    /**
     * 删除日志
     * @param id
     * @return
     */
    int deleteLogMD(@Param("id")Integer id);

    /**
     * 获取日志列表
     * @return
     */
    List<LogMD> getLogs();
}
