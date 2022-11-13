package com.zk.common.domain.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_park")
@Entity
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkId;

    private Integer parkStatus;

    private Integer deletedStatus;

    private Date createTime;

    private Date updateTime;
}
