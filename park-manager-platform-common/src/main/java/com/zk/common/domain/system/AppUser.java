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
@Table(name = "sys_app_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appUserId;

    private String userName;

    private String phone;

    private Integer sex;

    private String wechatName;

    private String wechatId;

    private Integer deletedStatus;

    private Date updateTime;

    private Date createTime;

}
