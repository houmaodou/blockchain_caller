package com.taikang.pension.blockchain.admin.service;

import com.taikang.pension.blockchain.admin.entity.Site;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houhx02
 * @since 2017-12-30
 */
public interface SiteService extends IService<Site> {

    Site getCurrentSite();

    void updateSite(Site site);
	
}
