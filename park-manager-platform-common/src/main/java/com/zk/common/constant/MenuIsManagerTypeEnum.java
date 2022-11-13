package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 流程状态
 */
@Getter
public enum MenuIsManagerTypeEnum {

    MANAGER_MENU("后端管理菜单", 1),
    APP_MENU("小程序菜单", 0),
    ;

    private String name;

    private Integer value;

    MenuIsManagerTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkIsMenuTypeValue(Integer value) {
        if (Arrays.stream(MenuIsManagerTypeEnum.values()).noneMatch(e -> e.getValue().equals(value))) {
            throw new RuntimeException("菜单的类型不");
        }
        return value;
    }
}
