package com.taikang.pension.blockchain.admin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taikang.pension.blockchain.admin.entity.Menu;
import com.taikang.pension.blockchain.admin.entity.VO.ShowMenu;
import com.taikang.pension.blockchain.admin.entity.VO.TreeMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author houhx02
 * @since 2017-10-31
 */

public interface MenuDao extends BaseMapper<Menu> {

    List<Menu> showAllMenusList(Map map);

    List<Menu> getMenus(Map map);

    List<ShowMenu> selectShowMenuByUser(Map<String,Object> map);
}