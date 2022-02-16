package com.example.springbootv2.service.meta;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.constant.Type;
import com.example.springbootv2.constant.WebConstant;
import com.example.springbootv2.dao.MetasDao;
import com.example.springbootv2.dao.RelationshipsDao;
import com.example.springbootv2.dto.MetaDto;
import com.example.springbootv2.dto.cond.MetaCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.ContentMD;
import com.example.springbootv2.model.MetaMD;
import com.example.springbootv2.model.RelationShipMD;
import com.example.springbootv2.service.content.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetasServiceImpl implements MetaService{
    @Autowired
    private MetasDao metasDao;
    @Autowired
    private RelationshipsDao relationshipsDao;
    @Autowired
    private ContentService contentService;

    @Override
    @CacheEvict(value = {"metaCaches","metaCache"},allEntries = true,beforeInvocation = true)
    public void addMeta(MetaMD meta) {
        if(meta == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        metasDao.addMeta(meta);
    }

    @Override
    @CacheEvict(value = {"metaCaches","metaCache"},allEntries = true,beforeInvocation = true)
    public void saveMeta(String type, String name, Integer mid) {
        if(StringUtils.isBlank(type) || StringUtils.isBlank(name))
            return ;
        MetaCond metaCond = new MetaCond(name,type);
        List<MetaMD>metaMDList = this.getMetas(metaCond);
        if(metaMDList != null && metaMDList.size() > 0)
            throw BusinessException.withErrorCode(ErrorConstant.Meta.META_IS_EXIST);
        MetaMD metaMD = new MetaMD();
        metaMD.setName(name);
        if(mid == null){
            //不存在该meta,则插入新的
            metaMD.setType(type);
            metasDao.addMeta(metaMD);
        }
        else{
            //如果已经存在该meta，则只对它的信息进行修改
            MetaMD meta = metasDao.getMetaById(mid);
            if(meta != null)
                metaMD.setMid(mid);
            metasDao.updateMeta(metaMD);
            if(meta != null)
                contentService.updateCategory(meta.getName(),name);
        }
    }

    @Override
    @CacheEvict(value = {"metaCaches","metaCache"},allEntries = true,beforeInvocation = true)
    public void addMetas(Integer cid, String names, String type) {
            if(cid == null)
                throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
            if(StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)){
                String [] nameArr = StringUtils.split(names,",");
                for(String name : nameArr){
                    this.saveOrUpdate(cid,name,type);
                }
            }
    }

    @Override
    @CacheEvict(value = {"metaCaches","metaCache"},allEntries = true,beforeInvocation = true)
    public void saveOrUpdate(Integer cid, String name, String type) {
        MetaCond metaCond = new MetaCond(name,type);
        List<MetaMD> metaMDList = this.getMetas(metaCond);

        int mid;
        MetaMD metaMD;
        if(metaMDList.size() == 1){
            MetaMD meta = metaMDList.get(0);
            mid = meta.getMid();
        }
        else if(metaMDList.size() > 1){
            throw BusinessException.withErrorCode(ErrorConstant.Meta.NOT_ONE_RESULT);
        }
        else{
            metaMD = new MetaMD();
            metaMD.setSlug(name);
            metaMD.setName(name);
            metaMD.setType(type);
            this.addMeta(metaMD);
            mid = metaMD.getMid();
        }
        if(mid != 0){
            Long count = relationshipsDao.getCountById(cid,mid);
            if(count == 0){
                RelationShipMD relationShipMD = new RelationShipMD();
                relationShipMD.setMid(mid);
                relationShipMD.setCid(cid);
                relationshipsDao.addRelationShip(relationShipMD);
            }
        }
    }

    @Override
    @CacheEvict(value = {"metaCaches","metaCache"},allEntries = true,beforeInvocation = true)
    public void deleteMetaById(Integer mid) {
        if(mid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        MetaMD meta = metasDao.getMetaById(mid);
        if(meta != null){
            String type = meta.getType();
            String name = meta.getName();
            metasDao.deleteMetaById(mid);
            //删除相关的数据
            List<RelationShipMD>relationShipMDS = relationshipsDao.getRelationShipByMid(mid);
            if(relationShipMDS != null && relationShipMDS.size() > 0){
                for(RelationShipMD var : relationShipMDS){
                    ContentMD article = contentService.getArticleById(var.getCid());
                    if(article != null){
                        ContentMD tmp = new ContentMD();
                        tmp.setCid(var.getCid());
                        if(type.equals(Type.CATEGORY.getType())){
                            tmp.setCategories(reMeta(name,article.getCategories()));
                        }
                        if(type.equals(Type.TAG.getType())){
                            tmp.setTags(reMeta(name, article.getTags()));
                        }
                        contentService.updateContent(tmp);
                    }
                    relationshipsDao.deleteRelationShipByMid(mid);
                }
            }
        }
    }

    @Override
    public void updateMeta(MetaMD meta) {
        if (null == meta || null == meta.getMid())
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        metasDao.updateMeta(meta);
    }

    @Override
    @Cacheable(value = "metaCache",key="'metaById_' + #p0")
    public MetaMD getMetaById(Integer mid) {
        if(mid == null)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return metasDao.getMetaById(mid);
    }

    @Override
    @Cacheable(value = "metaCache",key="'metas_' + #p0")
    public List<MetaMD> getMetas(MetaCond metaCond) {
        return metasDao.getMetasByCond(metaCond);
    }

    @Override
    @Cacheable(value = "metaCache",key="'metaList_' + #p0")
    public List<MetaDto> getMetaList(String type, String orderby, int limit) {
        if(StringUtils.isBlank(type)) return null;
        if(StringUtils.isBlank(orderby))
            orderby = "count desc,a.mid desc";
        if(limit < 1 || limit > WebConstant.MAX_POSTS)
            limit = 10;
        Map<String,Object>paraMap = new HashMap<>();
        paraMap.put("type",type);
        paraMap.put("order",orderby);
        paraMap.put("limit",limit);
        return metasDao.selectFromSql(paraMap);
    }
    private String reMeta(String name,String metas){
        String [] ms = StringUtils.split(metas,",");
        StringBuilder stringBuilder = new StringBuilder();
        for(String var : ms){
            if(!name.equals(var)){
                stringBuilder.append(",").append(var);
            }
        }
        if(stringBuilder.length() > 0){
            return stringBuilder.substring(1);
        }
        return "";
    }
}
