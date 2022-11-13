package com.zk.service.system;

import com.zk.common.domain.system.Role;
import com.zk.common.vo.DeptRoleList;
import com.zk.common.vo.RoleVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
public interface RoleService {
    List<Role> findAll(Integer deptId);

    Object getMenu();

    void editRoleInfo(RoleVo roleVO);

    void deleteRole(Integer roleId);

    Object getMenuList(Integer roleId);

    Object getDataTypeWithRole(Integer roleId);

    Page<DeptRoleList> findAllByDeptIdAndKey(Integer deptId, String keyword, Integer page, Integer size);
}
