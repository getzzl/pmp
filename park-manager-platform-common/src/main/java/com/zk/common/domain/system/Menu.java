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
@Table(name = "sys_menu")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    private String menuName;

    private Integer parentId;

    private String path;

    private Integer menuType;

    private String icon;

    private String remark;

    private Integer outChainStatus;

    private String outChainUrl;

    private Integer hideStatus;

    private Integer menuStatus;

    private Integer deletedStatus;

    private Integer order;

    private Date updateTime;

    private Date createTime;

    private String ids;

    private Integer isManagerMenu;

    private String menuCode;

}
