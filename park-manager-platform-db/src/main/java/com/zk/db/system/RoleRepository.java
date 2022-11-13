package com.zk.db.system;

import com.zk.common.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {


   Optional<Role> findByRoleIdAndDeletedStatus(Integer roleId, Integer deletedStatus);


}
