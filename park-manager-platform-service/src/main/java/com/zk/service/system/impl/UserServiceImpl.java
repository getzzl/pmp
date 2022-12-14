package com.zk.service.system.impl;

import com.zk.common.constant.MenuIsManagerTypeEnum;
import com.zk.common.constant.RedisConstants;
import com.zk.common.constant.SysConstants;
import com.zk.common.constant.UserTypeEnum;
import com.zk.common.domain.system.*;
import com.zk.common.util.EncryptUtils;
import com.zk.common.vo.AppUserList;
import com.zk.common.vo.ManagerUserList;
import com.zk.common.vo.UserEditInfo;
import com.zk.common.vo.UserMangerInfo;
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
import org.springframework.transaction.annotation.Transactional;
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
    public UserMangerInfo getUserInfo(User user) {
        return findUserDtoByUserId(user.getUserId());

    }


    @Override
    public UserMangerInfo findUserDtoByUserId(Integer userId) {

        Object redisResult = this.redisTemplate.opsForValue().get(RedisConstants.USER_ROLE_MENU_KEY + userId);


        if (redisResult == null) {
            log.info("????????? ???????????????????????????....");
            UserMangerInfo userMangerInfo = new UserMangerInfo();
            User userExist = userRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("????????????????????????"));
            userMangerInfo.setUserName(userExist.getUserName());
            userMangerInfo.setUserId(userId);
            userMangerInfo.setPhone(userExist.getPhone());

            Set<UserMangerInfo.UserRoleDto> roles = new HashSet<>();
            Set<UserMangerInfo.RoleMenuDto> roleMenuDtos = new HashSet<>();


            List<UserRole> userRoles = this.userRoleRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {

                roles = userRoles.stream().map(e -> {

                    UserMangerInfo.UserRoleDto userRoleDto = new UserMangerInfo.UserRoleDto();
                    userRoleDto.setRoleId(e.getRoleId());

                    //??????????????????
                    Optional<Role> roleOptional = roleRepository.findByRoleIdAndDeletedStatus(e.getRoleId(), SysConstants.DELETE_STATUS_ZERO);
                    if (roleOptional.isPresent()) {
                        userRoleDto.setRoleName(roleOptional.get().getRoleName());
                    } else {
                        log.info(" ????????????????????????????????? roleId:{}", e.getRoleId());
                        return null;
                    }

//                    //???????????????
//                    Set<Integer> menuIds = roleMenuRepository.findByRoleIdAndDeletedStatus(e.getRoleId(), SysConstants.DELETE_STATUS_ZERO).stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
//                    if (CollectionUtils.isEmpty(menuIds)) {
//                        log.info(" ?????????????????????????????????????????? roleId:{}", e.getRoleId());
//                        userRoleDto.setRoleMenuDtos(new ArrayList<>());
//                    } else {
//                        List<Menu> menuList = menuRepository.findByMenuIdInAndDeletedStatus(menuIds, SysConstants.DELETE_STATUS_ZERO);
//                        if (CollectionUtils.isEmpty(menuList)) {
//                            log.info(" ?????????????????????????????????????????? menuList roleId:{}", e.getRoleId());
//                            userRoleDto.setRoleMenuDtos(new ArrayList<>());
//                        } else {
//                            userRoleDto.setRoleMenuDtos(menuList.stream().map(menu -> {
//                                UserDto.RoleMenuDto roleMenuDto = new UserDto.RoleMenuDto();
//                                roleMenuDto.setMenuCode(menu.getMenuCode());
//                                roleMenuDto.setMenuName(menu.getMenuName());
//                                roleMenuDto.setMenuId(menu.getMenuId());
//                                roleMenuDto.setIsManagerMenu(menu.getIsManagerMenu());
//                                return roleMenuDto;
//                            }).collect(Collectors.toList()));
//                        }
//
//                    }
                    return userRoleDto;
                }).filter(Objects::nonNull).collect(Collectors.toSet());
            }


            if (!CollectionUtils.isEmpty(roles)) {
                //???????????????????????????
                Set<Integer> roleIds = roles.stream().map(UserMangerInfo.UserRoleDto::getRoleId).collect(Collectors.toSet());
                //???????????????
                List<RoleMenu> roleMenuList = this.roleMenuRepository.findByRoleIdInAndDeletedStatus(roleIds, SysConstants.DELETE_STATUS_ZERO);
                if (!CollectionUtils.isEmpty(roleMenuList)) {
                    Set<Integer> menuIds = roleMenuList.stream()
                            .filter(e -> MenuIsManagerTypeEnum.MANAGER_MENU.getValue().equals(e.getIsManagerMenu()))
                            .map(RoleMenu::getMenuId).collect(Collectors.toSet());


                    List<Menu> menuList = menuRepository.findByMenuIdInAndDeletedStatus(menuIds, SysConstants.DELETE_STATUS_ZERO);
                    if (!CollectionUtils.isEmpty(menuList)) {

                        Set<Menu> parentMenu = menuList.stream().filter(menu -> SysConstants.MENU_PARENT_ID.equals(menu.getParentId())).collect(Collectors.toSet());

                        //????????????
                        roleMenuDtos = parentMenu.stream().map(e -> {
                            UserMangerInfo.RoleMenuDto roleMenuDto = new UserMangerInfo.RoleMenuDto();
                            BeanUtils.copyProperties(e, roleMenuDto);
                            Set<UserMangerInfo.RoleMenuDto> childMenuByParentMenu = getChildMenuByParentMenu(e, new HashSet<>(menuList));
                            roleMenuDto.setChild(childMenuByParentMenu);
                            return roleMenuDto;
                        }).collect(Collectors.toSet());

                    }

                }

            }


            userMangerInfo.setUserRoleDtos(roles);
            userMangerInfo.setRoleMenuDtos(roleMenuDtos);

            redisTemplate.opsForValue().set(RedisConstants.USER_ROLE_MENU_KEY + userId, userMangerInfo);

            return userMangerInfo;
        } else {
            return (UserMangerInfo) redisResult;
        }
    }


    private Set<UserMangerInfo.RoleMenuDto> getChildMenuByParentMenu(Menu parent, Set<Menu> allMenu) {
        Set<UserMangerInfo.RoleMenuDto> child = new HashSet<>();


        Set<Menu> childMenuTemp = allMenu.stream().filter(e -> parent.getMenuId().equals(e.getParentId())).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(childMenuTemp)) {
            return child;
        }

        child = childMenuTemp.stream().map(e -> {
            UserMangerInfo.RoleMenuDto roleMenuDto = new UserMangerInfo.RoleMenuDto();
            BeanUtils.copyProperties(e, roleMenuDto);
            Set<UserMangerInfo.RoleMenuDto> childMenuByParentMenu = getChildMenuByParentMenu(e, allMenu);
            roleMenuDto.setChild(childMenuByParentMenu);
            return roleMenuDto;
        }).collect(Collectors.toSet());
        return child;
    }


    @Override
    public Page<ManagerUserList> findAllManagerUser(Integer page, Integer size, String keyword) {

        Page<User> usersPage = this.userRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(keyword)) {
                //???????????? like --or
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

                //??????????????????
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
                arrayList.add(build.equal(root.get("phone"), phone));
            }

            if (identityStatus != null) {
                arrayList.add(build.equal(root.get("identityStatus"), identityStatus));
            }

            if (!StringUtils.isEmpty(userName)) {
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
    @Transactional(rollbackFor = Exception.class)
    public User editUser(UserEditInfo user) {
        Integer userId = user.getUserId();
        Integer userInsertId = user.getUserId();
        List<Integer> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            throw new RuntimeException("????????????????????????????????????");
        }

        User result;

        if (userId == null) {

            //check phone
            List<User> userByPhoneList = this.userRepository.findByPhoneAndDeletedStatus(user.getPhone(), SysConstants.DELETE_STATUS_ZERO);

            if (!CollectionUtils.isEmpty(userByPhoneList)) {
                throw new RuntimeException("???????????????????????????????????????????????????");
            }

            //add
            User userBuild = User.builder().phone(user.getPhone()).userName(user.getUserName()).createTime(new Date()).updateTime(new Date()).password(EncryptUtils.md5(user.getPassword())).deletedStatus(SysConstants.DELETE_STATUS_ZERO).build();
            result = this.userRepository.save(userBuild);
            userInsertId = result.getUserId();
        } else {
            //update
            User userExist = this.userRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("???????????????????????????????????????????????????????????????"));

            if (!userExist.getPhone().equals(user.getPhone())) {
                //?????????????????????
                List<User> userSamePhoneExist = this.userRepository.findByPhoneAndDeletedStatusAndUserIdNot(user.getPhone(), SysConstants.DELETE_STATUS_ZERO, userId);
                if (!CollectionUtils.isEmpty(userSamePhoneExist)) {
                    throw new RuntimeException("??????????????????????????????????????????");
                }
            }

            userExist.setUserName(user.getUserName());
            userExist.setPassword(EncryptUtils.md5(user.getPassword()));
            userExist.setUpdateTime(new Date());
            userExist.setPhone(user.getPhone());
            result = this.userRepository.save(userExist);

            List<UserRole> userRoles = this.userRoleRepository.findByUserIdAndDeletedStatus(userId, SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<UserRole> userRoleUpdate = userRoles.stream().map(userRole -> {
                    userRole.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
                    userRole.setUpdateTime(new Date());
                    return userRole;
                }).collect(Collectors.toList());

                log.info("???????????????????????? ?????? size :{}", userRoles.size());
                this.userRoleRepository.saveAll(userRoleUpdate);
            }
        }


        final Integer userAndRoleId = userId == null ? userInsertId : userId;
        //app insert user-role
        if (!CollectionUtils.isEmpty(roles)) {
            List<UserRole> roleMenus = roles.stream().map(e -> {
                return UserRole.builder()
                        .roleId(e)
                        .deletedStatus(SysConstants.DELETE_STATUS_ZERO)
                        .userId(userAndRoleId)
                        .managerUser(UserTypeEnum.MANAGER_USER.getValue())
                        .createTime(new Date())
                        .updateTime(new Date())
                        .build();
            }).collect(Collectors.toList());

            this.userRoleRepository.saveAll(roleMenus);
        }


        return result;
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
