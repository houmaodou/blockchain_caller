package com.taikang.pension.blockchain.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.taikang.pension.blockchain.admin.entity.TransferRecord;
import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 * 转移记录 服务类
 * </p>
 *
 * @author houhx02
 * @since 2018-12-01
 */
public interface TransferRecordService extends IService<TransferRecord> {
public void insertRecordAndDetail(TransferRecord record, String detail);

}
