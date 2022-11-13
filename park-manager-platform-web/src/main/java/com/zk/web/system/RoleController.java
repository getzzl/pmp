package com.zk.web.system;

import com.zk.common.anataion.LogOperation;
import com.zk.common.constant.LogConstant;
import com.zk.common.result.ResultVo;
import com.zk.common.util.PageUtil;
import com.zk.common.vo.DeptRoleList;
import com.zk.common.vo.PageVo;
import com.zk.common.vo.RoleVo;
import com.zk.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zzl
 * Date: 2022/11/8 0008
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController {


    @Autowired
    private RoleService roleService;

    @GetMapping("/getRoleList")
    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "根据当前的组织获取角色列表", method = MediaType.APPLICATION_JSON_VALUE)
    public ResultVo<Page<DeptRoleList>> findAllByDeptIdAndKey(Integer deptId) {
        PageVo pageVo = PageUtil.getPage();
        return ResultVo.success(this.roleService.findAllByDeptIdAndKey(deptId, pageVo.getKeyword(), pageVo.getPage(), pageVo.getSize()));
    }

//    @GetMapping("/getMenu")
//    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "新增时，获取菜单权限列表", method = MediaType.APPLICATION_JSON_VALUE)
//    public ResultVo<Object> getMenu() {
//        return ResultVo.success(roleService.getMenu());
//    }


    @PostMapping("/editRoleInfo")
    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "编辑角色", method = MediaType.APPLICATION_JSON_VALUE)
    public ResultVo<Void> edit(@RequestBody RoleVo roleVO) {
        //todo 这里返回的是树形结构{menuId,isManagerMenu,menuName,children<List>} 还是 list 集合 包含了{ menuId,menuName,parentMenuId,isManagerMenu}
        roleService.editRoleInfo(roleVO);
        return ResultVo.success("成功");
    }

    @DeleteMapping("/deleteRole/{roleId}")
    @LogOperation(opType = LogConstant.OPTYPE.DELETE_LOG, summary = "删除角色", method = MediaType.APPLICATION_JSON_VALUE)
    public ResultVo<Void> delete(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
        return ResultVo.success("删除成功");
    }


    @GetMapping("/getMenuList/{roleId}")
    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "角色详情--获取菜单权限信息", method = MediaType.APPLICATION_JSON_VALUE)
    public ResultVo<Object> getMenuList(@PathVariable Integer roleId) {
        //todo 这里返回的是树形结构{menuId,isManagerMenu,menuName,children<List>} 还是 list 集合 包含了{ menuId,menuName,parentMenuId,isManagerMenu}
        return ResultVo.success(roleService.getMenuList(roleId));
    }



}
