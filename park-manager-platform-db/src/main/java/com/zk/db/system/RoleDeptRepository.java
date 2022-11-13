package com.zk.db.system;

import com.zk.common.domain.system.RoleDept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
public interface RoleDeptRepository extends JpaRepository<RoleDept,Integer> {

    List<RoleDept>  findByDeptIdAndDeletedStatus(Integer deptId,Integer deletedStatus);
}
