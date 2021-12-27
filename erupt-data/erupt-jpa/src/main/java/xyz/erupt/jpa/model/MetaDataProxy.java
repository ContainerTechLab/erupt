package xyz.erupt.jpa.model;

import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.core.context.MetaContext;

import java.time.LocalDateTime;

/**
 * @author YuePeng
 * date 2021/12/20 22:04
 */
public class MetaDataProxy implements DataProxy<MetaModel> {

    @Override
    public void beforeAdd(MetaModel metaModel) {
        metaModel.setCreateTime(LocalDateTime.now());
        metaModel.setUpdateBy(MetaContext.get().getMetaUser().getName());
        metaModel.setUpdateTime(metaModel.getCreateTime());
        metaModel.setUpdateBy(MetaContext.get().getMetaUser().getName());
    }

    @Override
    public void beforeUpdate(MetaModel metaModel) {
        metaModel.setUpdateTime(LocalDateTime.now());
        metaModel.setUpdateBy(MetaContext.get().getMetaUser().getName());
    }

}
