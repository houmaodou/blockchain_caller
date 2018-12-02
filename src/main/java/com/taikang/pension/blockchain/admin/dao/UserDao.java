package com.taikang.pension.blockchain.admin.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.taikang.pension.blockchain.admin.entity.Role;
import com.taikang.pension.blockchain.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author houhx02
 * @since 2017-10-31
 */
public interface UserDao extends BaseMapper<User> {
	User selectUserByMap(Map<String, Object> map);

	void saveUserRoles(@Param("userId")Long id, @Param("roleIds")Set<Role> roles);

	void dropUserRolesByUserId(@Param("userId")Long userId);

	Map selectUserMenuCount();
}