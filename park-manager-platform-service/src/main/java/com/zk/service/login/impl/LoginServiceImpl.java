package com.zk.service.login.impl;

import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.User;
import com.zk.db.system.UserRepository;
import com.zk.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Map<String, Object>> getUserPower(String userName) {
        return null;
    }

    @Override
    public User queryUser(String userName) {
        return userRepository.findByUserNameAndDeletedStatus(userName, SysConstants.DELETE_STATUS_ZERO).orElse(null);
    }
}
