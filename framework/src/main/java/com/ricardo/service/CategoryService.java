package com.ricardo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.Category;
import com.ricardo.domain.vo.CategoryVo;
import com.ricardo.domain.vo.UpdateCategoryVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author ricardo
 * @since 2023-03-20 19:35:18
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询分类名
     * @return
     */
    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult listPage(Integer pageNum, Integer pageSize);

    ResponseResult getUpdateById(Integer id);

    ResponseResult updateCategory(UpdateCategoryVo updateCategoryVo);
}

