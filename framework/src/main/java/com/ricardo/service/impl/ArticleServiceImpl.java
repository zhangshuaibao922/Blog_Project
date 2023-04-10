package com.ricardo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.AddArticleDto;
import com.ricardo.domain.entity.ArticleTag;
import com.ricardo.domain.vo.ArticleDetailVo;
import com.ricardo.domain.vo.ArticleListVo;
import com.ricardo.domain.vo.HotArticleVo;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.entity.Article;
import com.ricardo.domain.entity.Category;
import com.ricardo.mapper.ArticleMapper;
import com.ricardo.service.ArticleService;
import com.ricardo.service.ArticleTagService;
import com.ricardo.service.CategoryService;
import com.ricardo.utils.BeanCopyUtils;
import com.ricardo.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-20 10:44:07
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询热门文章
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //必须是正式文章
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按找浏览量进行排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCount);
        //最多只查前10条
        Page<Article> pageInfo=new Page<>(1,10);
        List<Article> articleList = page(pageInfo, lambdaQueryWrapper).getRecords();
//        List<HotArticleVo> hotArticleVoList =new ArrayList<>();
        //Bean拷贝
//        for (Article article : articleList) {
//            HotArticleVo hotArticleDto=new HotArticleVo();
//            BeanUtils.copyProperties(article,hotArticleDto);
//            hotArticleVoList.add(hotArticleDto);
//        }
        List<HotArticleVo> hotArticleVoList = BeanCopyUtils.copyBeanList(articleList, HotArticleVo.class);
        hotArticleVoList.stream().map(hotArticleVo -> {
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT, hotArticleVo.getId().toString());
            if(viewCount!=null){
                hotArticleVo.setViewCount(viewCount.longValue());
            }
            return hotArticleVo;
        }).collect(Collectors.toList());
        return ResponseResult.okResult(hotArticleVoList);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //如果有categoryId 查询时就要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对is_top进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> pageInfo = new Page<>(pageNum,pageSize);
        Page<Article> articlePage = page(pageInfo, lambdaQueryWrapper);

        List<Article> records = articlePage.getRecords();
        //查询categoryName
        records = records.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //获取id查询分类信息，获取name
            //把分类名称设置给article
        //查询categoryId对应的cateName
//        for (Article record : records) {
//            Category category = categoryService.getById(record.getCategoryId());
//            record.setCategoryName(category.getName());
//        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(records, ArticleListVo.class);
        articleListVos.stream().map(articleListVo -> {
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT, articleListVo.getId().toString());
            if(viewCount!=null){
                articleListVo.setViewCount(viewCount.longValue());
            }
            return articleListVo;
        }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(articleListVos,pageInfo.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id查询文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT, id.toString());
        if(viewCount!=null){
            article.setViewCount(viewCount.longValue());
        }
        //转换成dto
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装相应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT, String.valueOf(id),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto article) {
        //添加博客
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        save(article1);
        //添加博客和标签的关联
        List<ArticleTag> articleTags = article.getTags().stream().map(aLong -> new ArticleTag(article1.getId(), aLong)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}