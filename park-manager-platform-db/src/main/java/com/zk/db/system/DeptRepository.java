package com.zk.db.system;

import com.zk.common.domain.system.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept, Integer> {

    Optional<Dept> findByDeptIdAndDeletedStatus(Integer deptId, Integer deleteStatus);

    List<Dept> findByDeptParentId(Integer parentId);

    List<Dept> findByDeptParentIdAndDeptName(Integer parentId, String deptName);

    List<Dept> findByDeptParentIdAndDeptNameAndDeptIdNot(Integer parentId, String deptName, Integer deptId);

    List<Dept> findByIdsLike(String ids);


    Optional<Dept> findByDeptId(Integer deptId);
}
