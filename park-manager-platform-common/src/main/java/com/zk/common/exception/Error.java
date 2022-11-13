package com.zk.common.exception;

import lombok.Getter;

@Getter
public enum Error {

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(100, "未知异常"),

    /**
     * 添加异常
     */
    ADD_ERROR(103, "添加失败"),

    /**
     * 更新异常
     */
    UPDATE_ERROR(104, "更新异常"),

    /**
     * 删除异常
     */
    DELETE_ERORR(105, "删除异常"),

    /**
     * 查找异常
     */
    QUERY_ERROR(106, "查找异常"),
    
    /**
     * 文件解析错误
     */
    RESOLVE_ERROR(107, "解析错误"),
    /**
     * 用户名或者密码错误
     */
    ACCOUNT_ERROR(108, "用户名或者密码错误"),
    /**
     * 账号异常
     */
    ACCOUNT_USER_ERROR(109, "账号异常,账号被锁定或者限制登录"),

    /**
     * 执行错误
     */
    EXECUTE_ERROR(110, "执行错误");

    private Integer code;

    private String msg;

    private Error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
