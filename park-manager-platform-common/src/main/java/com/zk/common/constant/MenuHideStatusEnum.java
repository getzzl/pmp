package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 流程状态
 */
@Getter
public enum MenuHideStatusEnum {

    HIDE_STATUS("隐藏菜单", 0),
    NOT_HIDE_STATUS("不隐藏刚菜单", 1),

    ;

    private String name;

    private Integer value;

    MenuHideStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkMenuHideStatusValue(Integer value) {
        if (Arrays.stream(MenuHideStatusEnum.values()).noneMatch(e -> e.getValue().equals(value))) {
            throw new RuntimeException("菜单启用状态不合法");
        }
        return value;
    }
}
