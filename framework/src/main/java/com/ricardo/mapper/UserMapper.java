package com.ricardo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ricardo.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author ricardo
 * @since 2023-03-21 20:04:56
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

