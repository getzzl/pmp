package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 流程状态
 */
@Getter
public enum MenuOutChainStatusEnum {

    DIS_ENABLE_CHAIN("禁止外链", 0),
    ENABLE_CHAIN("启用外链", 1),

    ;

    private String name;

    private Integer value;

    MenuOutChainStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkMenuOutChainStatusValue(Integer value) {
        if (Arrays.stream(MenuOutChainStatusEnum.values()).noneMatch(e -> e.getValue().equals(value))) {
            throw new RuntimeException("菜单外链状态不合法");
        }
        return value;
    }
}
