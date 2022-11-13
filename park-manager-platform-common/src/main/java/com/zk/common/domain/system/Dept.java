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
@Table(name = "sys_dept")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deptId;

    private String deptName;

    private Integer parkId;

    private Integer deptParentId;

    private Integer deletedStatus;

    private String ids;

    private Date updateTime;

    private Date createTime;

}
