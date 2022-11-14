package com.zk.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author zzl
 * @date 2021/5/12 8:49
 * 用户的类型
 */
@Getter
public enum UserTypeEnum {

    APP_USER("小程序的用户", 0),
    MANAGER_USER("管理后台的用户", 1),

    ;

    private String name;

    private Integer value;

    UserTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static Integer checkUserTypeValue(Integer value) {
        if (Arrays.stream(UserTypeEnum.values()).noneMatch(e -> e.getValue().equals(value))) {
            throw new RuntimeException("用户类型不合法");
        }
        return value;
    }
}
