package com.taikang.pension.blockchain.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.taikang.pension.blockchain.admin.base.DataEntity;

/**
 * <p>
 * 转移记录
 * </p>
 *
 * @author houhx02
 * @since 2018-12-01
 */
@TableName("transfer_record")
public class TransferRecord extends DataEntity<TransferRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
	@TableField("id")
	private Long id;
    /**
     * 转移机构代码
     */
	@TableField("transfer_org_code")
	private String transferOrgCode;
    /**
     * 机构名称
     */
	@TableField("transfer_org_name")
	private String transferOrgName;
    /**
     * 转移方向
     */
	private Integer direction;
    /**
     * Excel 文件
     */
	@TableField("file_path")
	private String filePath;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getTransferOrgCode() {
		return transferOrgCode;
	}

	public void setTransferOrgCode(String transferOrgCode) {
		this.transferOrgCode = transferOrgCode;
	}
	public String getTransferOrgName() {
		return transferOrgName;
	}

	public void setTransferOrgName(String transferOrgName) {
		this.transferOrgName = transferOrgName;
	}
	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	@Override
	public String toString() {
		return "TransferRecord{" +
			", id=" + id +
			", transferOrgCode=" + transferOrgCode +
			", transferOrgName=" + transferOrgName +
			", direction=" + direction +
			", filePath=" + filePath +
			"}";
	}
}
