package com.example.springbootv2.service.user;

import com.example.springbootv2.constant.ErrorConstant;
import com.example.springbootv2.dao.UsersDao;
import com.example.springbootv2.dto.cond.UserCond;
import com.example.springbootv2.exception.BusinessException;
import com.example.springbootv2.model.UserMD;
import com.example.springbootv2.utils.TaleUtils;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersDao usersDao;

    @Override
    public int updateUserInfo(UserMD user) {
        if(user.getUid() == null)
            throw BusinessException.withErrorCode("用户编号不能为空");
        return usersDao.updateUserInfo(user);
    }

    @Override
    public UserMD getUserInfoById(Integer uId) {
        return usersDao.getUserInfoById(uId);
    }

    @Override
    public UserMD login(String username, String password) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        }
        String pwd = TaleUtils.MD5encode(username + password);
        UserMD user = usersDao.getUserInfoByCond(new UserCond(username,pwd));
        if(user == null)
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        return user;
    }
}
