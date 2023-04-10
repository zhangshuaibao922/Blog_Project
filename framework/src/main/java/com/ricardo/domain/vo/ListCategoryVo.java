package com.ricardo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCategoryVo {
    private Long id;

    //分类名
    private String name;

    private String description;
    //状态0:正常,1禁用
    private String status;
}
