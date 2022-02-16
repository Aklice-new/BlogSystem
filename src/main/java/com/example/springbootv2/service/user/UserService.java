package com.example.springbootv2.service.user;

import com.example.springbootv2.model.UserMD;

public interface UserService {
    /**
     * @Author: winterchen
     * @Description: 更改用户信息
     * @Date: 2018/4/20
     * @param user
     */
    int updateUserInfo(UserMD user);

    /**
     * @Author: winterchen
     * @Description: 根据主键编号获取用户信息
     * @Date: 2018/4/20
     * @param uId 主键
     */
    UserMD getUserInfoById(Integer uId);


    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    UserMD login(String username, String password);
}
