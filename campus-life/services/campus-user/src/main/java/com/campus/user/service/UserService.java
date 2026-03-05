package com.campus.user.service;

import com.campus.user.vo.UserVO;
import java.util.List;

public interface UserService {
    UserVO getUserInfo(Long userId);
    List<UserVO> listUsers();
    void updateUserInfo(Long userId, UserVO userVO);
}
