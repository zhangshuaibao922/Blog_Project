package com.ricardo.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    public static <T> T copyBean(Object source, Class<T> clazz) {
        //创建目标对象
        T instance = null;
        try {
            instance = clazz.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(source, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //返回结果
        return instance;
    }

    public static <T,V> List<T> copyBeanList(List<V> list, Class<T> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

}
