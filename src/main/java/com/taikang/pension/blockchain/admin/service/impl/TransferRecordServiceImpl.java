package com.taikang.pension.blockchain.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taikang.pension.blockchain.admin.entity.TransferRecord;
import com.taikang.pension.blockchain.admin.dao.TransferRecordDao;
import com.taikang.pension.blockchain.admin.service.TransferRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 转移记录 服务实现类
 * </p>
 *
 * @author houhx02
 * @since 2018-12-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransferRecordServiceImpl extends ServiceImpl<TransferRecordDao, TransferRecord> implements TransferRecordService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void insertRecordAndDetail(TransferRecord record, String detail) {
         this.insert(record);
         this.insertDetail(record,detail);
    }

    private void insertDetail(TransferRecord record, String detail) {
        String sql ="insert into transfer_record_detail values("+record.getId()+",'"+detail+"',now(),"+record.getCreateId()+",0)";
        jdbcTemplate.update(sql);
    }
}
