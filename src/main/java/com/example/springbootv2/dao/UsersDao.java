package com.example.springbootv2.dao;

import com.example.springbootv2.dto.cond.UserCond;
import com.example.springbootv2.model.UserMD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersDao {
    /**
     * @param user
     */
    int updateUserInfo(UserMD user);

    /**
     * @param uId 主键
     */
    UserMD getUserInfoById(@Param("uid") Integer uId);

    /**
     * 根据用户名和密码获取用户信息
     * @param userCond
     * @return
     */
    UserMD getUserInfoByCond(UserCond userCond);
}
