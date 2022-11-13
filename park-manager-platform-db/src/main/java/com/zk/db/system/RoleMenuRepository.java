package com.zk.db.system;

import com.zk.common.domain.system.RoleMenu;
import com.zk.common.domain.system.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu,Integer> {


    List<RoleMenu> findByRoleId(Integer userId);
}
