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
@Table(name = "pmp_watch")

public class PmpWatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer watchId;


    private String watchName;


    private String mobile;


    private String title;


    private String positionName;


    private Date createTime;

}