package com.ricardo.controller;


import com.ricardo.domain.ResponseResult;
import com.ricardo.service.ArticleService;
import com.ricardo.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章表(Article)表控制层
 *
 * @author ricardo
 * @since 2023-03-20 10:47:12
 */

@RestController
@RequestMapping("/article")
@Api(tags = "文章",description = "文章相关接口")
public class ArticleController {
    /**
     * 服务对象
     */
    @Resource
    private ArticleService articleService;


    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章列表",notes = "获取热门文章")
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @ApiOperation(value = "文章列表",notes = "获取文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小"),
            @ApiImplicitParam(name = "categoryId",value = "文章类别")
    }
    )
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}