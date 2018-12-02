package com.taikang.pension.blockchain.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taikang.pension.blockchain.admin.dao.AppSettingDAO;
import com.taikang.pension.blockchain.admin.dao.CollectionDAO;
import com.taikang.pension.blockchain.admin.entity.CollectionDTO;
import com.taikang.pension.blockchain.admin.service.AppSettingService;
import com.taikang.pension.blockchain.admin.service.CollectionService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houhx
 * @since 2018-11-18
 */
@Service("settingService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AppSettingServiceImpl extends ServiceImpl<AppSettingDAO, JSONObject> implements AppSettingService {
}
