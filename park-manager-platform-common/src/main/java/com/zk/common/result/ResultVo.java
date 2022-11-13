package com.zk.common.result;

import com.zk.common.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 固定返回格式
 *
 * @author zzl
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo<T> {

    /**
     * 响应数据
     */
    private T data;

    /**
     * 返回码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    public static final int SUCCESS = 200;

    public static final int DEFAULT_FAIL = 400;

    public static final String REQ_EXE_SUCCESS = "请求执行成功!";
    public static final String MSG_NONE = "";


    public static <T> ResultVo<T> success() {
        return success(REQ_EXE_SUCCESS);
    }

    public static <T> ResultVo<T> success(String msg) {
        return success(null, msg);
    }

    public static <T> ResultVo<T> success(T data, String msg) {
        return success(data, msg, SUCCESS);
    }

    public static <T> ResultVo<T> success(T data) {
        return success(data, MSG_NONE, SUCCESS);
    }

    public static <T> ResultVo<T> successAndGc(T data, String msg) {
        Runtime.getRuntime().gc();
        return success(data, msg);
    }

    public static <T> ResultVo<T> success(T data, String msg, int code) {
        return new ResultVo<>(data, code, msg);
    }

    public static <T> ResultVo<T> fail() {
        return fail(Error.UNKNOWN_ERROR);
    }

    public static <T> ResultVo<T> fail(Error errorCode) {
        return fail(null, errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> ResultVo<T> fail(T data, int code, String msg) {
        return new ResultVo<>(data, code, msg);
    }

    public static <T> ResultVo<T> fail(String msg) {
        return new ResultVo<>(null, DEFAULT_FAIL, msg);
    }
}
