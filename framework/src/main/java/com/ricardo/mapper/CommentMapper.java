package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-22 12:08:07
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}

