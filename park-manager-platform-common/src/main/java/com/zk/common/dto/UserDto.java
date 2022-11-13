package com.zk.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer userId;

    private String userName;

    private String account;

    private List<UserRoleDto> roles;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public static class UserRoleDto {

        private Integer roleId;

        private String roleName;

        private List<RoleMenuDto> roleMenuDtos;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleMenuDto {
        private Integer menuId;

        private String menuName;

        private String menuCode;

        private Integer isManagerMenu;
    }
}
