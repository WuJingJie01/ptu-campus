package com.campus.user.service.impl;

import com.campus.common.error.CommonErrorCode;
import com.campus.common.exception.BusinessException;
import com.campus.user.service.UserService;
import com.campus.user.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserVO getUserInfo() {
        UserVO user = null;
        if (user == null) {
            throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
        }
        user = new UserVO();
        user.setNickname("Test User");
        return user;
    }
}
