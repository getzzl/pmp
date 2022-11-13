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
public class ManagerUserList {

    private Integer userId;

    private String userName;

    private String phone;

    private List<UserListRoleVo> userRoles;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserListRoleVo {
        private Integer roleId;

        private String roleName;
    }
}
