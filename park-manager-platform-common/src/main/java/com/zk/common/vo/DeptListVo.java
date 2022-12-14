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
@AllArgsConstructor
@NoArgsConstructor
public class DeptListVo extends DeptListBaseVo {

    private List<DeptListVo> child;

}
