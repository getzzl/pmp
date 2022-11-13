package com.zk.common.constant;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Author: zzl
 * @Date: 2022/11/9 0009
 */
public class LogConstant {

        @Getter
        public enum LOGTYPE {
            /**
             * 日志类型-操作
             */
            OPERATE_LOG(0, "操作日志"),

            /**
             * 日志类型-执行
             */
            EXECUTE_LOG(1, "执行日志");

            private Integer code;

            private String msg;

            LOGTYPE(Integer code, String msg) {
                this.code = code;
                this.msg = msg;
            }

            public static Optional<LOGTYPE> getMsgForCode(Integer code) {
                return Stream.of(LOGTYPE.values())
                        .filter(t -> t.getCode().equals(code))
                        .findAny();
            }
        }

        @Getter
        public enum OPTYPE {
            /**
             * 操作类型-创建
             */
            CREATE_LOG(1, "创建"),

            /**
             * 操作类型-查询
             */
            SEARCH_LOG(2, "查询"),

            /**
             * 操作类型-删除
             */
            DELETE_LOG(3, "删除"),

            /**
             * 操作类型-编辑
             */
            EDIT_LOG(4, "编辑"),

            /**
             * 操作类型-执行
             */
            EXECUTE_LOG(5, "执行");

            private Integer code;

            private String msg;

            private OPTYPE(Integer code, String msg) {
                this.code = code;
                this.msg = msg;
            }

            public static Optional<OPTYPE> getCodeForMsg(String msg) {
                return Stream.of(OPTYPE.values())
                        .filter(t -> t.getMsg().equals(msg))
                        .findAny();
            }

            public static Optional<OPTYPE> getMsgForCode(Integer code) {
                return Stream.of(OPTYPE.values())
                        .filter(t -> t.getCode().equals(code))
                        .findAny();
            }
        }


        @Getter
        public enum OPSTATUS {
            /**
             * 操作结果
             */
            SUCCESS(0, "操作成功"),

            /**
             * 操作结果
             */
            EXECUTE_LOG(1, "操作失败");

            private Integer code;

            private String msg;

            OPSTATUS(Integer code, String msg) {
                this.code = code;
                this.msg = msg;
            }

            public static Optional<OPSTATUS> getMsgForCode(Integer code) {
                return Stream.of(OPSTATUS.values())
                        .filter(t -> t.getCode().equals(code))
                        .findAny();
            }
        }
}
