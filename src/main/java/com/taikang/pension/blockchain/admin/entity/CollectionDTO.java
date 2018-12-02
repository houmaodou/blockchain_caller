package com.taikang.pension.blockchain.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.taikang.pension.blockchain.admin.base.BaseEntity;

import java.util.Date;

@TableName("ti_collection")
public class CollectionDTO extends BaseEntity<CollectionDTO> {

    @TableField("id")
    private long id;
    @TableField("userName")
    private String userName;

    @TableField("fromName")
    private String fromName;

    @TableField("type")
    private int type;

    @TableField("des")
    private String  des;

    @TableField("createTime")
    private Date createTime;

    @TableField("fileId")
    private String fileId;

    @TableField("path")
    private String path;

    @TableField("topic")
    private String topic;

    @TableField("status")
    private int status;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CollectionDTO{" +
                "id=" + id +
                ", userName=" + userName +
                ", fromName='" + fromName + '\'' +
                ", type=" + type +
                ", des='" + des + '\'' +
                ", createTime=" + createTime +
                ", fileId='" + fileId + '\'' +
                ", path='" + path + '\'' +
                ", topic='" + topic + '\'' +
                ", status=" + status +
                '}';
    }
}
