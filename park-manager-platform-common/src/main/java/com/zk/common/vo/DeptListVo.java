package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeptListVo {

    private Integer deptId;

    private String deptName;

    private Integer deptPartId;

    private Date createTime;

    private Date updateTime;

    private List<DeptListVo> child;
}
