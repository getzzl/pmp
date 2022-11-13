package com.zk.web.system;

import com.zk.common.anataion.LogOperation;
import com.zk.common.constant.LogConstant;
import com.zk.common.domain.system.Menu;
import com.zk.common.domain.system.User;
import com.zk.common.result.ResultVo;
import com.zk.common.util.PageUtil;
import com.zk.common.vo.*;
import com.zk.service.system.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "分页查找后台用户列表", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getManagerUserList")
    public ResultVo<Page<UserList>> findAllManagerUser() {
        PageVo page = PageUtil.getPage();
        return ResultVo.success(this.userService.findAllManagerUser(page.getPage(), page.getSize(),page.getKeyword()));
    }

    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "分页查找app用户列表", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getAppUserList")
    public ResultVo<Page<UserList>> findAllAppUser() {
        PageVo page = PageUtil.getPage();
        return ResultVo.success(this.userService.findAllAppUser(page.getPage(), page.getSize(),page.getKeyword()));
    }


    @LogOperation(opType = LogConstant.OPTYPE.CREATE_LOG, summary = "添加/修改用户(用户id是否为null区别)", method = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/editManagerUser")
    public ResultVo<Void> editUser(@RequestBody UserEditInfo user) {
        User save = userService.editUser(user);
        if (save == null) {
            return ResultVo.fail("添加失败,用户名已存在");
        }
        return ResultVo.success("添加成功");
    }






    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "获取当前用户菜单列表", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getUserMenus")
    public ResultVo<List<Menu>> getUserMenus() {
        //todo 获取当前用户的信息 需要的是 树形结构还是 ， list 有上下级关系的
        return ResultVo.success(this.userService.getUserMenus());
    }


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "获取当前用户", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getUserInfo")
    public ResultVo<UserInfo> getUserInfo() {
        return ResultVo.success(this.userService.getUserInfo());
    }

    @GetMapping("/allNoPage")
    @Operation(summary = "用户列表不分页")
    public ResultVo<List<User>> findAllUser() {
        return ResultVo.success(this.userService.findAllUser());
    }



//
//    @Operation(summary = "修改用户密码", method = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping("/updatePassword")
//    public ResultVo<Void> updatePassword(@RequestBody UserPasswordVo userPasswordVo) {
//        log.info("[修改密码：] userPasswordVo: [{}]", userPasswordVo);
//        this.userService.updatePassword(userPasswordVo);
//        return ResultVo.success();
//    }

    @LogOperation(opType = LogConstant.OPTYPE.DELETE_LOG, summary = "删除管理用户", method = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/deleteManagerUser/{userId}")
    public ResultVo<Void> deleteManagerUser(@PathVariable Integer userId) {
        userService.deleteManagerUser(userId);
        return ResultVo.success("删除成功");
    }

    @LogOperation(opType = LogConstant.OPTYPE.DELETE_LOG, summary = "删除小程序用户", method = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/deleteAppUser/{userId}")
    public ResultVo<Void> deleteAppUser(@PathVariable Integer userId) {
        userService.deleteAppUser(userId);
        return ResultVo.success("删除成功");
    }



    @LogOperation(opType = LogConstant.OPTYPE.EDIT_LOG, summary = "更新用户信息", method = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("/update")
    public ResultVo<Void> update(@RequestBody UserEditInfo user) {
        User update = userService.update(user);
        if (update == null) {
            return ResultVo.fail("更新失败");
        }
        return ResultVo.success("更新成功");
    }


    @LogOperation(opType = LogConstant.OPTYPE.SEARCH_LOG, summary = "获取用户信息", method = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/find/{userId}")
    public ResultVo<UserInfo> findById(@PathVariable Integer userId) {
        UserInfo user = userService.findById(userId);
        if (user == null) {
            return ResultVo.fail("获取失败");
        }
        return ResultVo.success(user, "获取成功");
    }


}
