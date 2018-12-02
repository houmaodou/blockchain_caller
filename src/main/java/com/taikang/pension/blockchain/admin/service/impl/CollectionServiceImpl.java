package com.taikang.pension.blockchain.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.taikang.pension.blockchain.admin.dao.CollectionDAO;
import com.taikang.pension.blockchain.admin.dao.UserDao;
import com.taikang.pension.blockchain.admin.entity.CollectionDTO;
import com.taikang.pension.blockchain.admin.entity.Role;
import com.taikang.pension.blockchain.admin.entity.User;
import com.taikang.pension.blockchain.admin.service.CollectionService;
import com.taikang.pension.blockchain.admin.service.UserService;
import com.taikang.pension.blockchain.admin.util.ToolUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houhx
 * @since 2018-11-18
 */
@Service("collectionService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CollectionServiceImpl extends ServiceImpl<CollectionDAO, CollectionDTO> implements CollectionService {
}
