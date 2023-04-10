package com.ricardo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.TagListDto;
import com.ricardo.domain.entity.Tag;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author ricardo
 * @since 2023-03-24 15:49:54
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult saveTag(TagListDto tagListDto);

    ResponseResult deleteById(List<Long> ids);

    ResponseResult getUPdateById(Long id);

    ResponseResult updateByTag(TagVo tagVo);

    ResponseResult listAllTag();
}

