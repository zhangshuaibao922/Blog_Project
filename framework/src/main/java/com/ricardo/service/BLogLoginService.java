package com.ricardo.service;

import com.ricardo.domain.ResponseResult;
import com.ricardo.domain.entity.User;

public interface BLogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
