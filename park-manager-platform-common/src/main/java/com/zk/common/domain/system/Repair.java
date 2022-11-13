package com.zk.common.domain.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_repair")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer repaidId;


    private String repairNumber;


    private String repairName;


    private String mobile;


    private Integer repairType;

    private Date createTime;

}