package com.ricardo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.CommentDto;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.entity.Comment;
import com.ricardo.domain.entity.LoginUser;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.mapper.CommentMapper;
import com.ricardo.service.CommentService;
import com.ricardo.service.UserService;
import com.ricardo.utils.BeanCopyUtils;
import com.ricardo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-22 12:08:09
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId)
                .eq(Comment::getRootId, SystemConstants.COMMENT_ROOT)
                .orderByDesc(Comment::getCreateTime);

        //评论类型
        lambdaQueryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> pageInfo=new Page<>(pageNum,pageSize);
        Page<Comment> commentPage = page(pageInfo, lambdaQueryWrapper);

        List<CommentDto> commentDtos =toCommentDtoList(commentPage.getRecords());

        //查询所有根评论对应的子评论集合。并且赋值给对应的属性
        commentDtos.stream().map(commentDto -> {
            //查询对应的子评论
            List<CommentDto> children= getChileren(commentDto.getId());
            commentDto.setChildren(children);
            return commentDto;
        }).collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo(commentDtos,commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Authentication authentication = SecurityUtils.getAuthentication();
        if(!(SecurityUtils.getAuthentication().getPrincipal() instanceof LoginUser)){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        comment.setCreateBy(SecurityUtils.getUserId());
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论id查询所对应的子评论的集合
     * @param id 根评论id
     * @return
     */
    private List<CommentDto> getChileren(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getRootId,id);
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(lambdaQueryWrapper);
        List<CommentDto> commentDtos = toCommentDtoList(list);
        return commentDtos;
    }

    private List<CommentDto> toCommentDtoList(List<Comment> comments){
        List<CommentDto> commentDtos = BeanCopyUtils.copyBeanList(comments, CommentDto.class);
        //遍历dto集合
        commentDtos.stream().map(commentDto -> {
            //通过createBy查询用户的昵称并赋值
            String nickName = userService.getById(commentDto.getCreateBy()).getNickName();
            commentDto.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //不为-1查询
            if(commentDto.getToCommentId()!=-1){
                String toCommentUserName = userService.getById(commentDto.getToCommentUserId()).getNickName();
                commentDto.setToCommentUserName(toCommentUserName);
            }
            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }
}

