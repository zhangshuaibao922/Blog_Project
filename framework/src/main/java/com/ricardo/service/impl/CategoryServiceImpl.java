package com.ricardo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.vo.CategoryVo;
import com.ricardo.domain.entity.Article;
import com.ricardo.domain.entity.Category;
import com.ricardo.domain.vo.ListCategoryVo;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.vo.UpdateCategoryVo;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.mapper.CategoryMapper;
import com.ricardo.service.ArticleService;
import com.ricardo.service.CategoryService;
import com.ricardo.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-20 19:35:18
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类名
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categoryList = listByIds(categoryIds);
        categoryList= categoryList.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装dto
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos=BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult listPage(Integer pageNum, Integer pageSize) {
        Page<Category> pageInfo=new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getId);
        Page<Category> categoryPage = page(pageInfo, lambdaQueryWrapper);

        List<ListCategoryVo> listCategoryVos = BeanCopyUtils.copyBeanList(categoryPage.getRecords(), ListCategoryVo.class);
        //封装
        PageVo pageVo=new PageVo(listCategoryVos, categoryPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getUpdateById(Integer id) {
        Category byId = getById(id);
        UpdateCategoryVo updateCategoryVo = BeanCopyUtils.copyBean(byId, UpdateCategoryVo.class);
        return ResponseResult.okResult(updateCategoryVo);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryVo updateCategoryVo) {
        if(!StringUtils.hasText(updateCategoryVo.getName())){
            throw new SystemException(AppHttpCodeEnum.CATE_NAME_NOT_NULL);
        }
        if(!StringUtils.hasText(updateCategoryVo.getDescription())){
            throw new SystemException(AppHttpCodeEnum.CATE_DES_NOT_NULL);
        }
        Category category = BeanCopyUtils.copyBean(updateCategoryVo, Category.class);
        save(category);
        return ResponseResult.okResult();
    }
}

