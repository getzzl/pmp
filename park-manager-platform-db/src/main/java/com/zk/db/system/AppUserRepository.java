package com.zk.db.system;

import com.zk.common.domain.system.AppUser;
import com.zk.common.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> , JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByAppUserIdAndDeletedStatus(Integer appUserId,Integer deletedStatus);

}
