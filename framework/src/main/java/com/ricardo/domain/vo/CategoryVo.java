package com.ricardo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    private Long id;

    //分类名
    private String name;

    private String description;
}
