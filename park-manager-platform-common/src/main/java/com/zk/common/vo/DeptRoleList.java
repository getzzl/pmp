package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeptRoleList {


    private Integer deptId;

    private String deptName;

    private Integer roleId;

    private String roleName;


}
