package com.campus.user.service.impl;

import com.campus.common.error.CommonErrorCode;
import com.campus.common.exception.BusinessException;
import com.campus.security.entity.SysUser;
import com.campus.security.mapper.SysUserMapper;
import com.campus.user.service.UserService;
import com.campus.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserVO getUserInfo(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
        }
        
        UserVO userVO = new UserVO();
        userVO.setNickname(sysUser.getNickname());
        userVO.setAvatar(sysUser.getAvatar());
        userVO.setEmail(sysUser.getEmail());
        userVO.setPhone(sysUser.getPhone());
        userVO.setUsername(sysUser.getUsername());
        return userVO;
    }

    @Override
    public List<UserVO> listUsers() {
        List<SysUser> sysUsers = sysUserMapper.selectList(null);
        List<UserVO> userVOList = new ArrayList<>();
        
        for (SysUser sysUser : sysUsers) {
            UserVO userVO = new UserVO();
            userVO.setNickname(sysUser.getNickname());
            userVO.setAvatar(sysUser.getAvatar());
            userVO.setEmail(sysUser.getEmail());
            userVO.setPhone(sysUser.getPhone());
            userVO.setUsername(sysUser.getUsername());
            userVOList.add(userVO);
        }
        
        return userVOList;
    }

    @Override
    public void updateUserInfo(Long userId, UserVO userVO) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
        }
        
        if (userVO.getNickname() != null) {
            sysUser.setNickname(userVO.getNickname());
        }
        if (userVO.getAvatar() != null) {
            sysUser.setAvatar(userVO.getAvatar());
        }
        if (userVO.getEmail() != null) {
            sysUser.setEmail(userVO.getEmail());
        }
        if (userVO.getPhone() != null) {
            sysUser.setPhone(userVO.getPhone());
        }
        
        sysUserMapper.updateById(sysUser);
    }
}
