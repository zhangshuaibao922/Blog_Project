package com.ricardo.controller;


import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.dto.TagListDto;
import com.ricardo.domain.vo.PageVo;
import com.ricardo.domain.vo.TagVo;
import com.ricardo.service.TagService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签(Tag)表控制层
 *
 * @author ricardo
 * @since 2023-03-24 21:16:20
 */

@RestController
@RequestMapping("/content/tag")
public class TagController {
    /**
     * 服务对象
     */
    @Resource
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult saveTag(@RequestBody TagListDto tagListDto){
        return tagService.saveTag(tagListDto);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult deleteTag(@PathVariable("ids") List<Long> ids) {
        return tagService.deleteById(ids);
    }
    @GetMapping("/{id}")
    public ResponseResult getUPdateById(@PathVariable("id") Long id){
        return tagService.getUPdateById(id);
    }
    @PutMapping
    public ResponseResult updateTag(@RequestBody TagVo tagVo) {
        return tagService.updateByTag(tagVo);
    }

    @GetMapping("/listAllTag")
//    @PreAuthorize("")
//    @PostAuthorize("")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}

