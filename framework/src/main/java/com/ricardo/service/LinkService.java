package com.ricardo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author ricardo
 * @since 2023-03-21 19:11:24
 */
public interface LinkService extends IService<Link> {

    /**
     * 查询所有友链
     * @return
     */
    ResponseResult getAllLink();

}

