package com.example.springbootv2.service.attach;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.dao.AttachsDao;
import com.example.springbootv2.dto.AttAchDto;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.AttachMD;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachServiceImpl implements AttachService{
    @Autowired
    private AttachsDao attachsDao;
    @Override
    @CacheEvict(value = {"attCaches","attCache"},allEntries = true,beforeInvocation = true)
    public void addAttAch(AttachMD attachMD) {
        if(attachMD == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachsDao.addAttAch(attachMD);
    }

    @Override
    @CacheEvict(value = {"attCaches","attCache"},allEntries = true,beforeInvocation = true)
    public void batchAddAttAch(List<AttachMD> list) {
        if(list == null || list.size() == 0)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachsDao.batchAddAttAch(list);
    }

    @Override
    @CacheEvict(value = {"attCaches","attCache"},allEntries = true,beforeInvocation = true)
    public void deleteAttAch(Integer id) {
        if(id == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachsDao.deleteAttAch(id);
    }

    @Override
    @CacheEvict(value = {"attCaches","attCache"},allEntries = true,beforeInvocation = true)
    public void updateAttAch(AttachMD attAchMD) {
        if(attAchMD == null || attAchMD.getId() == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        attachsDao.updateAttAch(attAchMD);
    }

    @Override
    @Cacheable(value = "attCaches", key = "'attAchById' + #p0")
    public AttAchDto getAttAchById(Integer id) {
        if(id == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return attachsDao.getAttAchById(id);
    }

    @Override
    public PageInfo<AttAchDto> getAtts(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<AttAchDto> atts = attachsDao.getAtts();
        PageInfo<AttAchDto>pageInfo = new PageInfo<>(atts);
        return pageInfo;

    }
}
