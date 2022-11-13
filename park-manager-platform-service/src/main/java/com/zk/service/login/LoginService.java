package com.zk.service.login;

import com.zk.common.domain.system.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
public interface LoginService {
    List<Map<String, Object>> getUserPower(String userName);

    User queryUser(String userName);
}
