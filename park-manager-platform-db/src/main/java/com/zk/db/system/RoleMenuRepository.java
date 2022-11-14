package com.zk.db.system;

import com.zk.common.domain.system.RoleMenu;
import com.zk.common.domain.system.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu,Integer> {


    List<RoleMenu> findByRoleIdAndDeletedStatus(Integer roleId,Integer deletedStatus);
    List<RoleMenu> findByRoleIdInAndDeletedStatus(Set<Integer> roleIds, Integer deletedStatus);
}
