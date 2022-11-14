package com.zk.service.system;

import com.zk.common.domain.system.Menu;
import com.zk.common.domain.system.User;
import com.zk.common.vo.UserMangerInfo;
import com.zk.common.vo.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
public interface UserService {
    Optional<User> getByUserName(String userName,Integer deletedStatus);

    Page<ManagerUserList> findAll(Integer page, Integer size, String nickName, Integer deptId);

    List<Menu> getUserMenus();

    UserMangerInfo getUserInfo(User user);

    List<User> findAllUser();

    User save(UserEditInfo user);

    void updatePassword(UserPasswordVo userPasswordVo);

    void deleteUser(Integer userId);

    User update(UserEditInfo user);


    UserMangerInfo findUserDtoByUserId(Integer userId);

    Page<ManagerUserList> findAllManagerUser(Integer page, Integer size, String keyword);

    Page<AppUserList> findAllAppUser(Integer page, Integer size, String userName,String phone,Integer identityStatus);

    User editUser(UserEditInfo user);

    void deleteManagerUser(Integer userId);

    void deleteAppUser(Integer userId);
}
