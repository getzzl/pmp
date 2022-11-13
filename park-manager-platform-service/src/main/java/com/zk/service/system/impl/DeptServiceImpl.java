package com.zk.service.system.impl;

import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.Dept;
import com.zk.common.domain.system.Park;
import com.zk.common.domain.system.RoleDept;
import com.zk.common.vo.DeptListBaseVo;
import com.zk.common.vo.DeptListVo;
import com.zk.db.system.DeptRepository;
import com.zk.db.system.ParkRepository;
import com.zk.db.system.RoleDeptRepository;
import com.zk.service.system.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        //构建父节点
        DeptListVo deptListVo = new DeptListVo();
        deptListVo.setDeptName(park.getParkName());
        deptListVo.setDeptId(park.getParkId());


        List<Dept> deptList = this.deptRepository.findByParkIdAndDeletedStatus(parkId, SysConstants.DELETE_STATUS_ZERO);

        //递归查询子节点
        List<DeptListVo> childByParent = getChildByParent(deptList, park.getParkName());

        deptListVo.setChild(childByParent);


        return Collections.singletonList(deptListVo);
    }


    private List<DeptListVo> getChildByParent(List<Dept> deptList, String parentName) {


        if (CollectionUtils.isEmpty(deptList)) {
            return Collections.emptyList();
        }


        return deptList.stream().map(e -> {

            DeptListVo deptListVo = new DeptListVo();

            BeanUtils.copyProperties(e, deptListVo);

            List<Dept> deptInners = this.deptRepository.findByDeptParentIdAndDeletedStatus(e.getDeptId(), SysConstants.DELETE_STATUS_ZERO);

            deptListVo.setDeptParentName(parentName);

            deptListVo.setChild(getChildByParent(deptInners, e.getDeptName()));

            return deptListVo;

        }).collect(Collectors.toList());

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
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Integer deptId) {


        Dept dept = this.deptRepository.findByDeptIdAndDeletedStatus(deptId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("该节点不存在，或者已被删除"));


        dept.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);


        List<Dept> allDeptList = this.deptRepository.findByIdsLike(dept.getIds() + "%");

        //check role
        allDeptList.forEach(e -> {
            List<RoleDept> roleList = this.roleDeptRepository.findByDeptIdAndDeletedStatus(e.getDeptId(), SysConstants.DELETE_STATUS_ZERO);
            if (!CollectionUtils.isEmpty(roleList)) {
                throw new RuntimeException(String.format("部门: %s  下绑定的有角色，请先清除角色", e.getDeptName()));
            }
        });

        this.deptRepository.saveAll(allDeptList.stream().peek(e -> e.setDeletedStatus(SysConstants.DELETE_STATUS_ONE)).collect(Collectors.toList()));


    }

    @Override
    public Page<DeptListBaseVo> getDeptListBySearch(String keyword, Integer page, Integer size) {

        final String finalKeyword = keyword;
        Page<Dept> deptList = this.deptRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(finalKeyword)) {
                //构建条件 like --or
                arrayList.add(build.like(root.get("deptName"), "%" + keyword + "%"));
            }
            arrayList.add(build.equal(root.get("deletedStatus"), SysConstants.DELETE_STATUS_ZERO));

            Predicate[] predicates = new Predicate[arrayList.size()];
            return build.and(arrayList.toArray(predicates));
        }, PageRequest.of(page == null || page - 1 < 0 ? 0 : page - 1, size == null ? 20 : size, Sort.by(Sort.Direction.DESC, "createTime")));
        List<DeptListBaseVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deptList.getContent())) {
            result = deptList.getContent().stream().map(menu -> {
                DeptListBaseVo deptListBaseVo = new DeptListBaseVo();
                BeanUtils.copyProperties(menu, deptListBaseVo);
                if (menu.getDeptParentId().equals(menu.getParkId())) {
                    //说明上级园区
                    this.parkRepository.findByParkIdAndParkStatusAndDeletedStatus(menu.getParkId(), SysConstants.NODE_STATUS_ZERO, SysConstants.DELETE_STATUS_ZERO).ifPresent(park -> {
                        deptListBaseVo.setDeptParentName(park.getParkName());
                    });
                } else {
                    this.deptRepository.findByDeptIdAndDeletedStatus(deptListBaseVo.getDeptId(), SysConstants.DELETE_STATUS_ZERO).ifPresent(dept -> {
                        deptListBaseVo.setDeptParentName(dept.getDeptName());
                    });
                }
                return deptListBaseVo;
            }).collect(Collectors.toList());
        }
        return new PageImpl<>(result, deptList.getPageable(), deptList.getTotalElements());
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
