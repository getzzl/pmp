package com.zk.common.domain.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */

@Data
@Table(name = "sys_user_role")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRoleId;

    private Integer roleId;

    private Integer userId;

    private Integer deletedStatus;

}
