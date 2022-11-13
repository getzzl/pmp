package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 流程状态
 */
@Getter
public enum MenuStatusEnum {

    MENU_DIS_ENABLE("禁用状态", 0),
    MENU_ENABLE("启用状态", 1),

    ;

    private String name;

    private Integer value;

    MenuStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkMenuStatusValue(Integer value) {
        if (Arrays.stream(MenuStatusEnum.values()).noneMatch(e -> e.getValue().equals(value))) {
            throw new RuntimeException("菜单启用/禁用状态不合法");
        }
        return value;
    }
}
