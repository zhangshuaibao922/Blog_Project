package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.Link;
import org.apache.ibatis.annotations.Mapper;

/**
 * 友链(Link)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-21 19:10:41
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}

