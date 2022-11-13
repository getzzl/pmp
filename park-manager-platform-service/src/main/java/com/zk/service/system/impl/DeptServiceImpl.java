package com.zk.service.system.impl;

import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.Dept;
import com.zk.common.domain.system.Park;
import com.zk.common.domain.system.RoleDept;
import com.zk.common.vo.DeptListVo;
import com.zk.db.system.DeptRepository;
import com.zk.db.system.ParkRepository;
import com.zk.db.system.RoleDeptRepository;
import com.zk.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Service
public class DeptServiceImpl implements DeptService {


    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private RoleDeptRepository roleDeptRepository;

    @Override
    public List<Dept> getAll() {
        return deptRepository.findAll();
    }

    @Override
    public List<DeptListVo> getDeptList(Integer parkId) {




        //查询对应的园区的信息
        Park park = parkRepository.findByParkIdAndParkStatusAndDeletedStatus(parkId, SysConstants.NODE_STATUS_ZERO, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> {
            throw new RuntimeException("未找到当前园区的信息");
        });

//        this.deptRepository


        //构建园区根节点信息

        //递归查询所有的子节点的信息




        return null;
    }


    @Override
    public void editDept(Dept dept) {
        this.checkName(dept);
        if (dept.getDeptId() == null) {

            //新增
            Integer deptParentId = dept.getDeptParentId();
            if (deptParentId >= SysConstants.DEFAULT_INCREMENT_PARK_ID) {
                //todo 查询父节点是否是园区节点
                parkRepository.findByParkIdAndParkStatusAndDeletedStatus(deptParentId, SysConstants.NODE_STATUS_ZERO, SysConstants.DELETE_STATUS_ZERO)
                        .orElseThrow(() -> new RuntimeException("未查询到对应的 园区节点"));

            }


            dept.setCreateTime(new Date());
            dept.setDeletedStatus(SysConstants.DELETE_STATUS_ZERO);
            Dept saveDept = this.deptRepository.save(dept);

            //修改 ids 和 parkId
            if (deptParentId >= SysConstants.DEFAULT_INCREMENT_PARK_ID) {
                saveDept.setIds(deptParentId + "." + saveDept.getDeptId());
                saveDept.setParkId(deptParentId);
            } else {
                deptRepository.findByDeptIdAndDeletedStatus(deptParentId, SysConstants.DELETE_STATUS_ZERO)
                        .ifPresent(e -> {
                            saveDept.setIds(e.getIds() + "." + saveDept.getDeptId());
                            saveDept.setParkId(e.getParkId());

                        });
            }

            this.deptRepository.save(saveDept);

        } else {
            //修改
            this.deptRepository.findById(dept.getDeptId()).ifPresent(dept1 -> {
                dept1.setDeptName(dept.getDeptName());
                this.deptRepository.save(dept1);
            });
        }
    }

    @Override
    public void deleteDept(Integer deptId) {


        List<Dept> allDeptList = this.deptRepository.findByIdsLike("%" + deptId + "%");
        allDeptList.forEach(e -> {
            List<RoleDept> roleList = this.roleDeptRepository.findByDeptIdAndDeletedStatus(e.getDeptId(), SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(roleList)) {
                throw new RuntimeException(String.format("部门: %s  下绑定的有角色，请先清除角色", e.getDeptName()));
            }
        });
        //todo 这里要使用逻辑删除
        this.deptRepository.deleteAll(allDeptList);



    }


    private void checkName(Dept dept) {
        List<Dept> deptList;
        if (dept.getDeptId() == null) {
            deptList = this.deptRepository.findByDeptParentIdAndDeptName(dept.getDeptParentId(), dept.getDeptName());
        } else {
            deptList = this.deptRepository.findByDeptParentIdAndDeptNameAndDeptIdNot(dept.getDeptParentId(), dept.getDeptName(), dept.getDeptId());
        }
        if (!CollectionUtils.isEmpty(deptList)) {
            throw new RuntimeException("同一部门下不能存在相同的名称");
        }
    }
}
