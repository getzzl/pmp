package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 流程状态
 */
@Getter
public enum MenuTypeEnum {

    DIRECTORY("目录", 1),
    MENU("菜单", 2),
    BUTTON("按钮", 3);

    private String name;

    private Integer value;

    MenuTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkMenuTypeValue(Integer value) {
        if (Arrays.stream(MenuTypeEnum.values()).noneMatch(e ->e.getValue().equals(value))) {
            throw new RuntimeException("菜单的类型不");
        }
        return value;
    }
}
