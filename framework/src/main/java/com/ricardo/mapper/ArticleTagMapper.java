package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-25 13:08:13
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}

