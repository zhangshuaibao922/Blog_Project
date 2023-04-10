package com.ricardo.controller;

import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.vo.AdminUserInfoVo;
import com.ricardo.domain.vo.MenuVo;
import com.ricardo.domain.vo.RoutersVo;
import com.ricardo.domain.vo.UserInfoVo;
import com.ricardo.domain.entity.LoginUser;
import com.ricardo.domain.entity.User;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.service.LoginService;
import com.ricardo.service.MenuService;
import com.ricardo.service.RoleService;
import com.ricardo.utils.BeanCopyUtils;
import com.ricardo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示 必须要输入用户名
//            throw new RuntimeException();
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取当前登录用户的信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo =new AdminUserInfoVo(perms,roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        //获取当前登录用户的信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //查询menu，结果是tree的形式
        List<MenuVo> menus=menuService.selectRouterMenuTreeByUserId(loginUser.getUser().getId());
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
}
