package com.ricardo.service.impl;

import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.LoginUser;
import com.ricardo.domain.entity.User;

import com.ricardo.service.LoginService;
import com.ricardo.utils.JwtUtil;
import com.ricardo.utils.RedisCache;
import com.ricardo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache cache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取id，生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId.toString());
        //用户信息存入redis
        cache.setCacheObject(SystemConstants.ADMIN_REDIS_CACHE_KEY +userId,loginUser);

        //把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //token解析userid
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long id = loginUser.getUser().getId();
        //删除redis中的用户信息
        cache.deleteObject(SystemConstants.ADMIN_REDIS_CACHE_KEY+id);

        return ResponseResult.okResult();
    }
}
