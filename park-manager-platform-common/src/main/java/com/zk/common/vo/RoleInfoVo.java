package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleInfoVo {

    private Integer roleId;

    /**
     * 小程序菜单树
     */
    private Set<Integer> appMenuIds;

    /***
     * 管理后台菜单树
     */
    private Set<Integer> managerMenuIds;

    private String roleName;

    private Integer deptId;

    private String deptName;


}
