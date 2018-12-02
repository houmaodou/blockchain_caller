package com.taikang.pension.blockchain.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.taikang.pension.blockchain.admin.entity.Menu;
import com.taikang.pension.blockchain.admin.entity.User;
import com.taikang.pension.blockchain.admin.entity.VO.ShowMenu;
import com.taikang.pension.blockchain.admin.entity.VO.TreeMenu;
import com.taikang.pension.blockchain.admin.entity.VO.ZtreeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houhx02
 * @since 2017-10-31
 */
public interface MenuService extends IService<Menu> {

    List<Menu> selectAllMenus(Map<String,Object> map);

    List<ZtreeVO> showTreeMenus();

    List<ShowMenu> getShowMenuByUser(Long id);

    void saveOrUpdateMenu(Menu menu);

    int getCountByPermission(String permission);

    int getCountByName(String name);

}
