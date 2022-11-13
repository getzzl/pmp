package com.zk.service.system;

import com.zk.common.domain.system.Dept;
import com.zk.common.vo.DeptListVo;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
public interface DeptService {
    List<Dept> getAll();

    List<DeptListVo> getDeptList(Integer parkId);

    void editDept(Dept dept);

    void deleteDept(Integer deptId);
}
