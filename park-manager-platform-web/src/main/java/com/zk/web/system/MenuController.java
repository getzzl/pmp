package com.zk.web.system;

import com.zk.common.anataion.LogOperation;
import com.zk.common.constant.LogConstant;
import com.zk.common.constant.MenuIsManagerTypeEnum;
import com.zk.common.result.ResultVo;
import com.zk.common.util.PageUtil;
import com.zk.common.vo.MenuListBaseVo;
import com.zk.common.vo.MenuListVo;
import com.zk.common.vo.MenuVo;
import com.zk.common.vo.PageVo;
import com.zk.service.system.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzl
 * Date: 2022/11/8 0008
 */
@RestController
@RequestMapping("/api/menu")
@Slf4j
public class MenuController {

    @Autowired
    private MenuService menuService;


    //todo 小程序菜单

    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "根据后端管理的菜单树", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getAllManagerMenuList")
    public ResultVo<List<MenuListVo>> getAllManageMenuList(Integer menuId) {
        return ResultVo.success(this.menuService.getAllMenuList(menuId, MenuIsManagerTypeEnum.MANAGER_MENU.getValue()));
    }


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "根据小程序的菜单树", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getAllAppMenuList")
    public ResultVo<List<MenuListVo>> getAllAppMenuList(Integer menuId) {
        return ResultVo.success(this.menuService.getAllMenuList(menuId, MenuIsManagerTypeEnum.APP_MENU.getValue()));
    }


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "搜索后端管理组织列表", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/getManagerMenuToSearch")
    public ResultVo<Page<MenuListBaseVo>> getManagerMenuToSearch(Integer menuStatus) {
        PageVo page = PageUtil.getPage();
        return ResultVo.success(this.menuService.getMenuToSearch(menuStatus, page.getPage(), page.getSize(), page.getKeyword(), MenuIsManagerTypeEnum.MANAGER_MENU.getValue()));
    }

    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "搜索app组织列表", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/getAppMenuToSearch")
    public ResultVo<Page<MenuListBaseVo>> getAppMenuToSearch(Integer menuStatus) {
        PageVo page = PageUtil.getPage();
        return ResultVo.success(this.menuService.getMenuToSearch(menuStatus, page.getPage(), page.getSize(), page.getKeyword(), MenuIsManagerTypeEnum.APP_MENU.getValue()));
    }


    @LogOperation(opType = LogConstant.OPTYPE.EDIT_LOG, summary = "编辑菜单，删除/新增（区别于是否有菜单id）", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/editMenu")
    @RequiresPermissions(value = "system.menu.edit")
    public ResultVo<Void> editMenu(@RequestBody MenuVo menuVo) {
        this.menuService.editMenu(menuVo);
        return ResultVo.success();
    }


    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "删除菜单", method = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/deleteMenu/{menuId}")
    public ResultVo<Void> deleteMenu(@PathVariable("menuId") Integer menuId) {
        this.menuService.deleteMenu(menuId);
        return ResultVo.success();
    }

    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "修改菜单启用状态", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/updateMenuStatus/{menuId}")
    public ResultVo<Void> updateMenuStatus(@PathVariable("menuId") Integer menuId, Integer menuStatus) {
        this.menuService.updateMenuStatus(menuId, menuStatus);
        return ResultVo.success();
    }

    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "修改菜单显示状态", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/updateMenuHideStatus/{menuId}")
    public ResultVo<Void> updateMenuHideStatus(@PathVariable("menuId") Integer menuId, Integer hideStatus) {
        this.menuService.updateMenuHideStatus(menuId, hideStatus);
        return ResultVo.success();
    }


}
