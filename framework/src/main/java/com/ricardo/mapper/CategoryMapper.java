package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类表(Category)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-20 19:35:18
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}

