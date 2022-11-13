package com.zk.common.domain.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */

@Data
@Table(name = "sys_user_park")
@AllArgsConstructor
@NoArgsConstructor
public class UserPark {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userParkId;


    private Integer parkId;

    private Integer userId;


}