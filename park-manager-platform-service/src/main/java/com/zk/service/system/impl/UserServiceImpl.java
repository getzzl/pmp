package com.zk.service.system.impl;

import com.zk.common.constant.RedisConstants;
import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.*;
import com.zk.common.dto.UserDto;
import com.zk.common.util.EncryptUtils;
import com.zk.common.vo.*;
import com.zk.db.system.*;
import com.zk.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;


    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<User> getByUserName(String userName, Integer deletedStatus) {
        return userRepository.findByUserNameAndDeletedStatus(userName, deletedStatus);
    }

    @Override
    public Page<ManagerUserList> findAll(Integer page, Integer size, String nickName, Integer deptId) {
        return null;
    }

    @Override
    public List<Menu> getUserMenus() {
        return null;
    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public List<User> findAllUser() {
        return null;
    }

    @Override
    public User save(UserEditInfo user) {
        return null;
    }

    @Override
    public void updatePassword(UserPasswordVo userPasswordVo) {

    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public User update(UserEditInfo user) {
        return null;
    }

    @Override
    public UserInfo findById(Integer userId) {
        return null;
    }

    @Override
    public UserDto findUserDtoByUserId(Integer userId) {

        Object redisResult = this.redisTemplate.opsForValue().get(RedisConstants.USER_ROLE_MENU_KEY + userId);


        if (redisResult == null) {
            log.info("数据库 查询对应的权限信息....");
            UserDto userDto = new UserDto();
            Optional<User> user = userRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);

            user.orElseThrow(() -> new RuntimeException("未找到对应的用户"));
            User user1 = user.get();
            userDto.setUserName(user1.getUserName());
            userDto.setUserId(userId);

            List<UserRole> userRoles = this.userRoleRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {

                List<UserDto.UserRoleDto> userRoleDtoList = userRoles.stream().map(e -> {

                    UserDto.UserRoleDto userRoleDto = new UserDto.UserRoleDto();
                    userRoleDto.setRoleId(e.getRoleId());

                    //查询角色详情
                    Optional<Role> roleOptional = roleRepository.findByRoleIdAndDeletedStatus(e.getRoleId(), SysConstants.DELETE_STATUS_ZERO);
                    if (roleOptional.isPresent()) {
                        userRoleDto.setRoleName(roleOptional.get().getRoleName());
                    } else {
                        log.info(" 未查询到对应的角色信息 roleId:{}", e.getRoleId());
                        return null;
                    }

                    //每一个角色
                    Set<Integer> menuIds = roleMenuRepository.findByRoleIdAndDeletedStatus(e.getRoleId(), SysConstants.DELETE_STATUS_ZERO).stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
                    if (CollectionUtils.isEmpty(menuIds)) {
                        log.info(" 该角色暂时没有对应的菜单权限 roleId:{}", e.getRoleId());
                        userRoleDto.setRoleMenuDtos(new ArrayList<>());
                    } else {
                        List<Menu> menuList = menuRepository.findByMenuIdInAndDeletedStatus(menuIds, SysConstants.DELETE_STATUS_ZERO);
                        if (CollectionUtils.isEmpty(menuList)) {
                            log.info(" 该角色暂时没有对应的菜单权限 menuList roleId:{}", e.getRoleId());
                            userRoleDto.setRoleMenuDtos(new ArrayList<>());
                        } else {
                            userRoleDto.setRoleMenuDtos(menuList.stream().map(menu -> {
                                UserDto.RoleMenuDto roleMenuDto = new UserDto.RoleMenuDto();
                                roleMenuDto.setMenuCode(menu.getMenuCode());
                                roleMenuDto.setMenuName(menu.getMenuName());
                                roleMenuDto.setMenuId(menu.getMenuId());
                                roleMenuDto.setIsManagerMenu(menu.getIsManagerMenu());
                                return roleMenuDto;
                            }).collect(Collectors.toList()));
                        }

                    }
                    return userRoleDto;
                }).filter(Objects::nonNull).collect(Collectors.toList());

                userDto.setRoles(userRoleDtoList);

                redisTemplate.opsForValue().set(RedisConstants.USER_ROLE_MENU_KEY + userId, userDto);
                return userDto;
            }
            return null;
        } else {
            return (UserDto) redisResult;
        }
    }

    @Override
    public Page<ManagerUserList> findAllManagerUser(Integer page, Integer size, String keyword) {

        Page<User> usersPage = this.userRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(keyword)) {
                //构建条件 like --or
                arrayList.add(build.equal(root.get("phone"), keyword));
            }

            Predicate[] predicates = new Predicate[arrayList.size()];
            return build.and(arrayList.toArray(predicates));
        }, PageRequest.of(page == null || page - 1 < 0 ? 0 : page - 1, size == null ? 20 : size, Sort.by(Sort.Direction.DESC, "createTime")));

        List<ManagerUserList> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(usersPage.getContent())) {
            result = usersPage.getContent().stream().map(user -> {
                ManagerUserList managerUserList = new ManagerUserList();
                BeanUtils.copyProperties(user, managerUserList);

                //设置角色关系
                List<UserRole> userRoles = userRoleRepository.findByUserIdAndDeletedStatus(user.getUserId(), SysConstants.DELETE_STATUS_ZERO);
                managerUserList.setUserRoles(CollectionUtils.isEmpty(userRoles) ? new ArrayList<>() : userRoles.stream().map(e -> ManagerUserList.UserListRoleVo.builder().roleId(e.getRoleId()).roleName(roleRepository.findById(e.getRoleId()).get().getRoleName()).build()).collect(Collectors.toList()));

                return managerUserList;
            }).collect(Collectors.toList());
        }
        return new PageImpl<>(result, usersPage.getPageable(), usersPage.getTotalElements());
    }

    @Override
    public Page<AppUserList> findAllAppUser(Integer page, Integer size, String userName, String phone, Integer identityStatus) {
        Page<AppUser> usersPage = this.appUserRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(phone)) {
                //构建条件 like --or
                arrayList.add(build.equal(root.get("phone"), phone));
            }

            if (identityStatus != null) {
                //构建条件 like --or
                arrayList.add(build.equal(root.get("identityStatus"), identityStatus));
            }

            if (!StringUtils.isEmpty(userName)) {
                //构建条件 like --or
                arrayList.add(build.like(root.get("userName"), "%" + userName + "%"));
            }

            Predicate[] predicates = new Predicate[arrayList.size()];
            return build.and(arrayList.toArray(predicates));
        }, PageRequest.of(page == null || page - 1 < 0 ? 0 : page - 1, size == null ? 20 : size, Sort.by(Sort.Direction.DESC, "createTime")));

        List<AppUserList> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(usersPage.getContent())) {
            result = usersPage.getContent().stream().map(user -> {
                AppUserList appUserList = new AppUserList();
                BeanUtils.copyProperties(user, appUserList);
                return appUserList;
            }).collect(Collectors.toList());
        }
        return new PageImpl<>(result, usersPage.getPageable(), usersPage.getTotalElements());
    }

    @Override
    public User editUser(UserEditInfo user) {
        Integer userId = user.getUserId();
        Integer userInsertId = user.getUserId();
        List<Integer> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            throw new RuntimeException("未给该用户分配相关的角色");
        }

        if (userId == null) {

            //check phone
            List<User> userByPhoneList = this.userRepository.findByPhoneAndDeletedStatus(user.getPhone(), SysConstants.DELETE_STATUS_ZERO);

            if (!CollectionUtils.isEmpty(userByPhoneList)) {
                throw new RuntimeException("该手机号已经注册，请更换相关手机号");
            }

            //add
            User userBuild = User.builder().phone(user.getPhone()).userName(user.getUserName()).password(EncryptUtils.md5(user.getPassword())).deletedStatus(SysConstants.DELETE_STATUS_ZERO).build();
            User userSave = this.userRepository.save(userBuild);
            userInsertId = userSave.getUserId();
        } else {
            //update
            User userExist = this.userRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("未找到需要修改的用户信息，或者该用户已删除"));

            if (!userExist.getPhone().equals(user.getPhone())) {
                //想要修改手机号
                List<User> userSamePhoneExist = this.userRepository.findByPhoneAndDeletedStatusAndUserIdNot(user.getPhone(), SysConstants.DELETE_STATUS_ZERO, userId);
                if (!CollectionUtils.isEmpty(userSamePhoneExist)) {
                    throw new RuntimeException("该手机号已被注册，请重新修改");
                }
            }

            userExist.setUserName(user.getUserName());
            userExist.setPassword(user.getPassword());
            userExist.setPhone(user.getPhone());
            User userSave = this.userRepository.save(userExist);

            List<UserRole> userRoles = this.userRoleRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<UserRole> userRoleUpdate = userRoles.stream().map(userRole -> {
                    userRole.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
                    return userRole;
                }).collect(Collectors.toList());

                log.info("需要修改的角色的 数量 size :{}", userRoles.size());
                this.userRoleRepository.saveAll(userRoleUpdate);
            }
        }


        final Integer userAndRoleId = userId == null ? userInsertId : userId;
        //app insert user-role
        if (!CollectionUtils.isEmpty(roles)) {
            List<UserRole> roleMenus = roles.stream().map(e -> {
                return UserRole.builder().roleId(e).deletedStatus(SysConstants.DELETE_STATUS_ZERO).userId(userAndRoleId).build();
            }).collect(Collectors.toList());

            this.userRoleRepository.saveAll(roleMenus);
        }


        return null;
    }

    @Override
    public void deleteManagerUser(Integer userId) {

        userRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO).ifPresent(user -> {
            user.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
            userRepository.save(user);
            List<UserRole> userRoles = userRoleRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<UserRole> userRoleUpdate = userRoles.stream().map(e -> {
                    e.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
                    return e;
                }).collect(Collectors.toList());

                this.userRoleRepository.saveAll(userRoleUpdate);
            }
        });
    }

    @Override
    public void deleteAppUser(Integer appUserId) {
        appUserRepository.findByAppUserIdAndDeletedStatus(appUserId, SysConstants.DELETE_STATUS_ZERO).ifPresent(user -> {
            user.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
            appUserRepository.save(user);

            List<UserRole> userRoles = userRoleRepository.findByUserIdAndDeletedStatus(appUserId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<UserRole> userRoleUpdate = userRoles.stream().map(e -> {
                    e.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
                    return e;
                }).collect(Collectors.toList());

                this.userRoleRepository.saveAll(userRoleUpdate);
            }
        });
    }
}
