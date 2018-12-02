package com.taikang.pension.blockchain.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.taikang.pension.blockchain.admin.entity.Log;

import java.util.List;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author houhx02
 * @since 2018-01-13
 */
public interface LogService extends IService<Log> {

    public List<Integer> selectSelfMonthData();

}
