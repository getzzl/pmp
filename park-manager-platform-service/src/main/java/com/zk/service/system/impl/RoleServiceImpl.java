package com.zk.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.zk.common.constant.MenuIsManagerTypeEnum;
import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.Dept;
import com.zk.common.domain.system.Role;
import com.zk.common.domain.system.RoleMenu;
import com.zk.common.domain.system.UserRole;
import com.zk.common.vo.DeptRoleList;
import com.zk.common.vo.RoleInfoVo;
import com.zk.common.vo.RoleVo;
import com.zk.db.system.*;
import com.zk.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<Role> findAll(Integer deptId) {
        return null;
    }

    @Override
    public Object getMenu() {
        return null;
    }

    @Override
    public void editRoleInfo(RoleVo roleVO) {
        if (StringUtils.isEmpty(roleVO.getRoleName())) {
            throw new RuntimeException("请输入角色名称");
        }

        Integer deptId = roleVO.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("请选择关联的部门");
        }

        Integer roleId = roleVO.getRoleId();
        Integer roleIdInsert = roleVO.getRoleId();


        Set<Integer> appMenuIds = getMenuTreeIds(roleVO.getAppMenuIds(), new HashSet<>());
        log.info("appMenuIds:{}", JSONObject.toJSON(appMenuIds));
        Set<Integer> managerMenuIds = getMenuTreeIds(roleVO.getManagerMenuIds(), new HashSet<>());

        if (roleId == null) {
            //add role
            Role role = Role.builder().deletedStatus(SysConstants.DELETE_STATUS_ZERO).roleName(roleVO.getRoleName()).deptId(deptId).createTime(new Date()).updateTime(new Date()).build();

            Role saveRole = roleRepository.save(role);
            roleIdInsert = saveRole.getRoleId();
        } else {
            //修改角色
            Role roleExist = this.roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("未找到对应的角色，或者角色已删除"));
            //check dept
            List<UserRole> userRoles = this.userRoleRepository.findByRoleIdAndDeletedStatus(roleId, SysConstants.DELETE_STATUS_ZERO);
            if (!roleExist.getDeptId().equals(deptId)) {
                //修改了组织关系
                if (!CollectionUtils.isEmpty(userRoles)) {
                    throw new RuntimeException("该角色绑定的有相关的用户，无法更换组织关系");
                } else {
                    //可以修改组织关系
                    roleExist.setDeptId(deptId);
                }
            }
            roleExist.setRoleName(roleVO.getRoleName());
            //修改角色信息
            this.roleRepository.save(roleExist);

            //删除这个角色 已有的权限
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<UserRole> updateUserRoles = userRoles.stream().map(e -> {
                    e.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
                    return e;
                }).collect(Collectors.toList());
                log.info("updateUserRoles size :{}", updateUserRoles.size());
                this.userRoleRepository.saveAll(updateUserRoles);
            }

        }

        final Integer userRoleId = roleId == null ? roleIdInsert : roleId;
        //app insert menu-role
        if (!CollectionUtils.isEmpty(appMenuIds)) {
            List<RoleMenu> roleMenus = appMenuIds.stream().map(e -> {
                return RoleMenu.builder().roleId(userRoleId).deletedStatus(SysConstants.DELETE_STATUS_ZERO).menuId(e).isManagerMenu(SysConstants.IS_MANAGER_MENU_ZERO).createTime(new Date()).updateTime(new Date()).build();
            }).collect(Collectors.toList());

            this.roleMenuRepository.saveAll(roleMenus);
        }

        //manager insert menu-role
        if (!CollectionUtils.isEmpty(managerMenuIds)) {
            List<RoleMenu> managerMenus = appMenuIds.stream().map(e -> {
                return RoleMenu.builder().roleId(userRoleId).deletedStatus(SysConstants.DELETE_STATUS_ZERO).menuId(e).isManagerMenu(SysConstants.IS_MANAGER_MENU_ONE).createTime(new Date()).updateTime(new Date()).build();
            }).collect(Collectors.toList());

            this.roleMenuRepository.saveAll(managerMenus);
        }

    }

    @Override
    public void deleteRole(Integer roleId) {
        Role roleExist = this.roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("未找到对应的角色，或者角色已删除"));

        List<UserRole> userRoles = this.userRoleRepository.findByRoleIdAndDeletedStatus(roleId, SysConstants.DELETE_STATUS_ZERO);
        if (!CollectionUtils.isEmpty(userRoles)) {
            throw new RuntimeException("该角色下面绑定的有用户,暂时无法删除");
        }

        roleExist.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
        this.roleRepository.save(roleExist);

    }


    private Set<Integer> getMenuTreeIds(List<RoleVo.AppMenuIds> appMenuIds, Set<Integer> result) {
        if (CollectionUtils.isEmpty(appMenuIds)) {
            return result;
        }
        appMenuIds.forEach(e -> {
            result.add(e.getMenuId());
            getMenuTreeIds(e.getChild(), result);

        });
        return result;
    }


    @Override
    public RoleInfoVo getMenuList(Integer roleId) {

        Role roleExist = this.roleRepository.findByRoleIdAndDeletedStatus(roleId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("未找到对应的角色,或者该角色已被删除"));


        Dept dept = this.deptRepository.findByDeptIdAndDeletedStatus(roleExist.getDeptId(), SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("该角色绑定的部门已被删除，请核对后操作"));

        RoleInfoVo roleInfoVo = new RoleInfoVo();
        roleInfoVo.setRoleId(roleId);
        roleInfoVo.setRoleName(roleExist.getRoleName());
        roleInfoVo.setDeptName(dept.getDeptName());
        roleInfoVo.setDeptId(roleExist.getDeptId());


        List<RoleMenu> roleMenuList = this.roleMenuRepository.findByRoleIdAndDeletedStatus(roleId, SysConstants.DELETE_STATUS_ZERO);

        if (CollectionUtils.isEmpty(roleMenuList)) {

            roleInfoVo.setAppMenuIds(Collections.emptySet());
            roleInfoVo.setManagerMenuIds(Collections.emptySet());

        } else {
            Set<Integer> managerMenuIds = roleMenuList.stream().filter(e -> MenuIsManagerTypeEnum.MANAGER_MENU.getValue().equals(e.getIsManagerMenu())).map(RoleMenu::getMenuId).collect(Collectors.toSet());
            Set<Integer> appMenuIds = roleMenuList.stream().filter(e -> MenuIsManagerTypeEnum.APP_MENU.getValue().equals(e.getIsManagerMenu())).map(RoleMenu::getMenuId).collect(Collectors.toSet());

            roleInfoVo.setAppMenuIds(CollectionUtils.isEmpty(appMenuIds) ? Collections.emptySet() :appMenuIds);
            roleInfoVo.setManagerMenuIds(CollectionUtils.isEmpty(managerMenuIds) ? Collections.emptySet() :managerMenuIds);

        }

        return roleInfoVo;
    }

    @Override
    public Object getDataTypeWithRole(Integer roleId) {
        return null;
    }

    @Override
    public Page<DeptRoleList> findAllByDeptIdAndKey(Integer deptId, String keyword, Integer page, Integer size) {

        final String finalKeyword = keyword;
        Optional<Dept> optionalDept = deptRepository.findByDeptId(deptId);
        optionalDept.orElseThrow(() -> new RuntimeException("未查询到指定的部门"));
        Page<Role> roles = this.roleRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(finalKeyword)) {
                //构建条件 like --or
                arrayList.add(build.like(root.get("roleName"), "%" + finalKeyword + "%"));
            }
            if (deptId != null) {
                arrayList.add(build.equal(root.get("deptId"), deptId));
            }
            Predicate[] predicates = new Predicate[arrayList.size()];
            return build.and(arrayList.toArray(predicates));
        }, PageRequest.of(page == null || page - 1 < 0 ? 0 : page - 1, size == null ? 20 : size, Sort.by(Sort.Direction.DESC, "createTime")));
        List<DeptRoleList> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles.getContent())) {
            result = roles.getContent().stream().map(role -> {
                DeptRoleList deptRoleList = new DeptRoleList();
                BeanUtils.copyProperties(role, deptRoleList);
                deptRoleList.setDeptId(deptId);
                deptRoleList.setDeptName(optionalDept.get().getDeptName());
                return deptRoleList;
            }).collect(Collectors.toList());
        }
        return new PageImpl<>(result, roles.getPageable(), roles.getTotalElements());


    }
}
