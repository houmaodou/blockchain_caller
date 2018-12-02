package com.taikang.pension.blockchain.admin.service;

import com.taikang.pension.blockchain.admin.entity.UploadInfo;
import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 * 文件上传配置 服务类
 * </p>
 *
 * @author houhx02
 * @since 2018-07-06
 */
public interface UploadInfoService extends IService<UploadInfo> {

    public UploadInfo getOneInfo();

    public void updateInfo(UploadInfo uploadInfo);

}
