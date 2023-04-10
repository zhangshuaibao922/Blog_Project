package com.ricardo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.AddArticleDto;
import com.ricardo.domain.entity.Article;

/**
 * 文章表(Article)表服务接口
 *
 * @author ricardo
 * @since 2023-03-20 10:44:07
 */
public interface ArticleService extends IService<Article> {
    /**
     * 查询热门文章
     * @return
     */
    ResponseResult hotArticleList();

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 根据id查询文章详情
     * @param id
     * @return
     */
    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);
}

