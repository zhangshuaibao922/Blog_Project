package com.ricardo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author ricardo
 * @since 2023-03-22 12:08:09
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

