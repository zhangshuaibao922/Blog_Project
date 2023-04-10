package com.ricardo.controller;

import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.User;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.service.BLogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    private BLogLoginService bLogLoginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示 必须要输入用户名
//            throw new RuntimeException();
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return bLogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return bLogLoginService.logout();
    }
}
