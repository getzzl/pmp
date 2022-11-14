package com.zk.common.vo;

import com.zk.common.dto.UserDto;
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
public class UserInfo {
    private Integer userId;

    private String userName;

    private String account;

    private String phone;


    private List<UserDto.UserRoleDto> roles;


    //菜单列表
    private List<UserInfoMenu> menuList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfoMenu {
        private Integer menuId;

        private String menuCode;

        private String menuName;

        private List<UserInfoMenu> child;
    }

}
