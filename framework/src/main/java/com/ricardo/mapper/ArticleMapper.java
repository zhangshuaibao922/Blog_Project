package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-20 10:44:05
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}

