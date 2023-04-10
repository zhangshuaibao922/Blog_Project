package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.vo.MenuVo;
import com.ricardo.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-24 17:06:25
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}

