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
@Table(name = "pmp_watch_plan")
public class PmpWatchPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer watchPlanId;


    private Integer watchNumber;


    private Integer watchType;


    private Date watchTime;

    private String watchLeader;


    private String watchMan;


    private Integer leaderId;


    private Integer watchId;


    private String thing;


    private String remark;


    private String record;


    private Byte isDel;


    private Date updateTime;

    private Date createTime;

}