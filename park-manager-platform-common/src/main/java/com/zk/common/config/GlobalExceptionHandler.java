package com.zk.common.config;

import com.zk.common.result.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义异常
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public ResultVo<Void> handleException(AuthorizationException e){
        log.error("Error caused by :=====", e);
        return ResultVo.fail("您权限不足,请联系管理员");
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResultVo<Void> handleException(NoSuchElementException e){
        e.printStackTrace();
        log.error("Error {}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResultVo<Void> handleException(Exception e){
        e.printStackTrace();
        log.error("Error {}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }


}
