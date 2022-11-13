package com.zk.common.anataion;

import com.zk.common.constant.LogConstant;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: cdj
 * @date: 2021/6/24 9:07
 * @Description:
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Operation
public @interface LogOperation {
    //日志类型
    LogConstant.LOGTYPE logType() default LogConstant.LOGTYPE.OPERATE_LOG;
    //操作日志
    LogConstant.OPTYPE opType();

    @AliasFor(attribute = "method", annotation = Operation.class)
    String method() default "";

    @AliasFor(attribute = "summary", annotation = Operation.class)
    String summary() default "";
}
