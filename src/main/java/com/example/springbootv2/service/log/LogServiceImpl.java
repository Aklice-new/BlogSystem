package com.example.springbootv2.service.log;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.dao.LogsDao;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.LogMD;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService{

    @Autowired
    LogsDao logsDao;

    @Override
    public void addLog(String action, String data, String ip, Integer authorId) {
        LogMD logMD = new LogMD();
        logMD.setAction(action);
        logMD.setAuthorId(authorId);
        logMD.setData(data);
        logMD.setIp(ip);
        logsDao.addLogMD(logMD);
    }

    @Override
    public void deleteLogById(Integer id) {
        if (null == id)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        deleteLogById(id);
    }

    @Override
    public PageInfo<LogMD> getLogs(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<LogMD> logs = logsDao.getLogs();
        PageInfo<LogMD>pageInfo = new PageInfo<>(logs);
        return pageInfo;
    }
}
