package com.zk.db.system;

import com.zk.common.domain.system.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Repository
public interface ParkRepository extends JpaRepository<Park,Integer> {

    Optional<Park> findByParkIdAndParkStatusAndDeletedStatus(Integer parkId,Integer parkStatus,Integer deletedStatus);

}
