package com.taikang.pension.blockchain.admin.service.impl;

import com.taikang.pension.blockchain.admin.entity.QuartzTaskLog;
import com.taikang.pension.blockchain.admin.dao.QuartzTaskLogDao;
import com.taikang.pension.blockchain.admin.service.QuartzTaskLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 任务执行日志 服务实现类
 * </p>
 *
 * @author houhx02
 * @since 2018-01-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuartzTaskLogServiceImpl extends ServiceImpl<QuartzTaskLogDao, QuartzTaskLog> implements QuartzTaskLogService {

}
