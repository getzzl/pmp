package com.zk.db.system;

import com.zk.common.domain.system.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {

    List<UserRole> findByUserIdAndDeletedStatus(Integer userId,Integer deletedStatus);

    List<UserRole> findByRoleIdAndDeletedStatus(Integer roleId,Integer deletedStatus);
}
