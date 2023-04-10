package com.ricardo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.vo.MenuVo;
import com.ricardo.domain.entity.Menu;
import com.ricardo.mapper.MenuMapper;
import com.ricardo.service.MenuService;
import com.ricardo.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-24 17:06:28
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有权限
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON)
                            .eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(lambdaQueryWrapper);
            List<String> perms = menuList.stream().map(menu -> menu.getPerms()).collect(Collectors.toList());
            return perms;
        }
        //否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        //判断是否是管理员，返回所有符合要求的menu
        List<MenuVo> menuVos =null;
        if (SecurityUtils.isAdmin()) {
            menuVos =menuMapper.selectAllRouterMenu();
        }else {
            //否则 当前用户所具有的menu
            menuVos =menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //找出第一层的菜单，然后去找他们的子菜单，设置到children中
        List<MenuVo> menus=buildMenuTree(menuVos,0L);
        return menus;
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menuVos, Long parentId) {
        List<MenuVo> menuVoList = menuVos.stream()
                .filter(menuDto -> menuDto.getParentId().equals(parentId))
                .map(menuDto -> menuDto.setChildren(getChildren(menuDto, menuVos)))
                .collect(Collectors.toList());
        return menuVoList;
    }

    /**
     * 获取menuDtos的子菜单menudto集合
     *
     * @param menuVo
     * @param menuVos
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> childrenList = menuVos.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m->m.setChildren(getChildren(m, menuVos)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

