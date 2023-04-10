package com.ricardo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectBatchByIds;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.TagListDto;
import com.ricardo.domain.entity.Tag;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.vo.TagVo;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.mapper.TagMapper;
import com.ricardo.service.TagService;
import com.ricardo.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author ricardo
 * @since 2023-03-24 15:49:54
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        Page<Tag> pageInfo = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName())
                .like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagList = page(pageInfo, lambdaQueryWrapper);
        //分装数据
        PageVo pageVo = new PageVo(tagList.getRecords(),tagList.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult saveTag(TagListDto tagListDto) {
        if(!StringUtils.hasText(tagListDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NUll);
        }
        if(!StringUtils.hasText(tagListDto.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NOT_NUll);
        }
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName,tagListDto.getName());
        int count = count(queryWrapper);
        if(count > 0){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_EXIST);
        }
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(List<Long> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUPdateById(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateByTag(TagVo tagVo) {
        if(!StringUtils.hasText(tagVo.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NUll);
        }
        if(!StringUtils.hasText(tagVo.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NOT_NUll);
        }
        Tag tag = BeanCopyUtils.copyBean(tagVo, Tag.class);
        tagMapper.updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}