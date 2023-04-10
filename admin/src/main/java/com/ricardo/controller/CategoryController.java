package com.ricardo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.Category;
import com.ricardo.domain.vo.CategoryVo;
import com.ricardo.domain.vo.ExcelCategoryVo;
import com.ricardo.domain.vo.UpdateCategoryVo;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.service.CategoryService;
import com.ricardo.utils.BeanCopyUtils;
import com.ricardo.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> categoryVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVos);
    }
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize){
        return categoryService.listPage(pageNum, pageSize);
    }
    @GetMapping("/{id}")
    public ResponseResult getUpdateById(@PathVariable("id") Integer id){
        return  categoryService.getUpdateById(id);
    }
    @PostMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryVo updateCategoryVo){
        return categoryService.updateCategory(updateCategoryVo);
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
