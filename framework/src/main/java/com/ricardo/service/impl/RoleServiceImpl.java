package com.ricardo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.domain.entity.Role;
import com.ricardo.mapper.RoleMapper;
import com.ricardo.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import com.ricardo.service.RoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-24 17:17:22
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要admin
        if(SecurityUtils.isAdmin()){
            List<String> roleKeys=new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //查询用户具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}

