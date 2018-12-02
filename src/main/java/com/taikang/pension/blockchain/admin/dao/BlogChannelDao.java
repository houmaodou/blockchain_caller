package com.taikang.pension.blockchain.admin.dao;

import com.taikang.pension.blockchain.admin.entity.BlogChannel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taikang.pension.blockchain.admin.entity.VO.ZtreeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客栏目 Mapper 接口
 * </p>
 *
 * @author houhx02
 * @since 2018-01-17
 */
public interface BlogChannelDao extends BaseMapper<BlogChannel> {

    List<ZtreeVO> selectZtreeData(Map<String,Object> map);

    List<BlogChannel> selectChannelData(Map<String, Object> map);
}
