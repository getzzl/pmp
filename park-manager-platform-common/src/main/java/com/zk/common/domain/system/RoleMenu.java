package com.zk.common.domain.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */

@Data
@Table(name = "sys_role_menu")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleMenuId;

    private Integer roleId;

    private Integer menuId;
    
    private Integer deletedStatus;

    private Date updateTime;

    private Date createTime;

    /**
     * 是否是 后端管理的菜单， 1 是，0不是
     */
    private Integer isManagerMenu;

}
