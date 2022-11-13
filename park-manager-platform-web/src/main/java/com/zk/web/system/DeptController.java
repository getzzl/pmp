package com.zk.web.system;

import com.zk.common.anataion.LogOperation;
import com.zk.common.constant.LogConstant;
import com.zk.common.domain.system.Dept;
import com.zk.common.result.ResultVo;
import com.zk.common.vo.DeptListVo;
import com.zk.service.system.DeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzl
 * Date: 2022/11/8 0008
 */
@RestController
@RequestMapping("/api/dept")
@Tag(name = "DeptController", description = "部门管理")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "根据当前的园区或者组织获取组织列表", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getDeptList")
    public ResultVo<List<DeptListVo>> getDeptList(Integer parkId) {
        return ResultVo.success(this.deptService.getDeptList(parkId));
    }

    @LogOperation(opType = LogConstant.OPTYPE.EDIT_LOG, summary = "编辑部门", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/editDept")
    @RequiresPermissions(value = "system.dept.edit")
    public ResultVo<Void> editDept(@RequestBody Dept dept) {
        this.deptService.editDept(dept);
        return ResultVo.success();
    }


    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "删除部门", method = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/deleteDept/{deptId}")
    public ResultVo<Void> deleteDept(@PathVariable("deptId") Integer deptId) {
        this.deptService.deleteDept(deptId);
        return ResultVo.success();
    }

}
