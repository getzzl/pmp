package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuVo {


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

    private Integer ids;

    private Integer isManagerMenu;

    private String menuCode;
}
