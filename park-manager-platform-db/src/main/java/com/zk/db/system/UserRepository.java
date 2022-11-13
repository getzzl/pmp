package com.zk.db.system;

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
public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {

    Optional<User> findByUserNameAndDeletedStatus(String userName,Integer deletedStatus);

    Optional<User> findByUserIdAndDeletedStatus(Integer userId,Integer deletedStatus);

    List<User> findByPhoneAndDeletedStatus(String phone, Integer deletedStatus);

    List<User> findByPhoneAndDeletedStatusAndUserIdNot(String phone, Integer deletedStatus,Integer userId);

}
