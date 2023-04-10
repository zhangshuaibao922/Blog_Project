package com.ricardo.service.impl;

import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.vo.BlogUserLoginVo;
import com.ricardo.domain.vo.UserInfoVo;
import com.ricardo.domain.entity.LoginUser;
import com.ricardo.domain.entity.User;
import com.ricardo.service.BLogLoginService;
import com.ricardo.utils.BeanCopyUtils;
import com.ricardo.utils.JwtUtil;
import com.ricardo.utils.RedisCache;
import com.ricardo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BLogLoginServiceImpl implements BLogLoginService {
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
        cache.setCacheObject(SystemConstants.REDIS_CACHE_KEY +userId,loginUser);
        //把token和userinfo封装，返回
        //将user转换为userinfodto
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo =new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //token解析userid
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long id = loginUser.getUser().getId();
        //删除redis中的用户信息
        cache.deleteObject(SystemConstants.REDIS_CACHE_KEY+id);

        return ResponseResult.okResult();
    }

}
