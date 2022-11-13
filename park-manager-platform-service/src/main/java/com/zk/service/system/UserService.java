package com.zk.service.system;

import com.zk.common.domain.system.Menu;
import com.zk.common.domain.system.User;
import com.zk.common.dto.UserDto;
import com.zk.common.vo.UserEditInfo;
import com.zk.common.vo.UserInfo;
import com.zk.common.vo.UserList;
import com.zk.common.vo.UserPasswordVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
public interface UserService {
    Optional<User> getByUserName(String userName);

    Page<UserList> findAll(Integer page, Integer size, String nickName, Integer deptId);

    List<Menu> getUserMenus();

    UserInfo getUserInfo();

    List<User> findAllUser();

    User save(UserEditInfo user);

    void updatePassword(UserPasswordVo userPasswordVo);

    void deleteUser(Integer userId);

    User update(UserEditInfo user);

    UserInfo findById(Integer userId);

    UserDto findUserDtoByUserId(Integer userId);

    Page<UserList> findAllManagerUser(Integer page, Integer size, String keyword);

    Page<UserList> findAllAppUser(Integer page, Integer size, String keyword);

    User editUser(UserEditInfo user);

    void deleteManagerUser(Integer userId);

    void deleteAppUser(Integer userId);
}
