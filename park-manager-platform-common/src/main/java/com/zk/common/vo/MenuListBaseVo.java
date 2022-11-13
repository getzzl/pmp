package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuListBaseVo {


    private Integer menuId;

    private String menuName;

    private Integer parentId;

    private String parentName;

    private String icon;

    private Integer menuType;

    private Integer hideStatus;

    private Integer outChainStatus;

    private String outChainUrl;

    /**
     * 路由地址
     */
    private String path;

    private String remark;

    private Integer menuStatus;

    private Integer order;

    private Date updateTime;

    private Date createTime;

    private String menuCode;


}
