package com.example.springbootv2.service.option;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.dao.OptionsDao;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.OptionMD;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements OptionService{

    @Autowired
    OptionsDao optionsDao;

    @Override
    @CacheEvict(value = {"optionsCache","optionsCaches"},allEntries = true,beforeInvocation = true)
    public void deleteOptionByName(String name) {
        if(StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        optionsDao.deleteOptionByName(name);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache","optionsCaches"},allEntries = true,beforeInvocation = true)
    public void updateOptionByName(String name, String value) {
        if(StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        OptionMD optionMD = new OptionMD();
        optionMD.setName(name);
        optionMD.setValue(value);
        optionsDao.updateOptionByName(optionMD);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache","optionsCaches"},allEntries = true,beforeInvocation = true)
    public void saveOptions(Map<String, String> options) {
        if(options != null && !options.isEmpty()){
            options.forEach(this::updateOptionByName);
//            options.forEach((k,v)->{
//                this.updateOptionByName(k,v);
//            });
        }
    }

    @Override
    @Cacheable(value = "optionCache",key = "'optionsByname_' + #p0")
    public OptionMD getOptionByName(String name) {
        if(StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return optionsDao.getOptionByName(name);
    }

    @Override
    @Cacheable(value = "optionsCache",key = "'options_' + #p0")
    public List<OptionMD> getOptions() {
        return optionsDao.getOptions();
    }
}
