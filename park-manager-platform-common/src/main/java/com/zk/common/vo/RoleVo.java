package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleVo {

    private Integer roleId;

    /**
     * 小程序菜单树
     */
    private List<AppMenuIds> appMenuIds;

    /***
     * 管理后台菜单树
     */
    private List<AppMenuIds> managerMenuIds;

    private String roleName;

    private Integer deptId;

    private String deptName;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AppMenuIds {
        private String menuName;

        private Integer menuId;

        private List<AppMenuIds> child;
    }

}
