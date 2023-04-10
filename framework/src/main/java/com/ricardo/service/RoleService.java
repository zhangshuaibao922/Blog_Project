package com.ricardo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author ricardo
 * @since 2023-03-24 17:17:21
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

