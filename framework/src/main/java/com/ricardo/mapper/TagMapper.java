package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签(Tag)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-24 15:49:54
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}

