package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMangerInfo {

    private Integer userId;

    private String userName;

    private String account;

    private Set<UserRoleDto> userRoleDtos;

    private Set<RoleMenuDto> roleMenuDtos;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public static class UserRoleDto {

        private Integer roleId;

        private String roleName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleMenuDto {
        private Integer menuId;

        private String menuName;

        private String menuCode;

        private Set<RoleMenuDto> child;
    }
}
