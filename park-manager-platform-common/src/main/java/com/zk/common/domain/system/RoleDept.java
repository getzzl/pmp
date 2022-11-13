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
@Table(name = "sys_role_dept")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleDeptId;

    private Integer roleId;

    private Integer deptId;

    private Integer deletedStatus;

    private Date updateTime;

    private Date createTime;


}
