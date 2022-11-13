//package com.zk.common.util;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.event.Level;
//import org.springframework.util.CollectionUtils;
//
///**
// * @Author: zzl
// * @Date: 2022/11/9 0009
// */
//public class AssertUtils {
//    /**
//     * <p>
//     * 若被检查对象为空白字符串,则抛异常
//     * </p>
//     *
//     * <pre>
//     * StringUtils.isBlank(null)      = true
//     * StringUtils.isBlank("")        = true
//     * StringUtils.isBlank(" ")       = true
//     * StringUtils.isBlank("bob")     = false
//     * StringUtils.isBlank("  bob  ") = false
//     * </pre>
//     *
//     * @param str
//     *            the String to check, may be null
//     * @return <code>true</code> if the String is null, empty or whitespace
//     * @since 2.0
//     */
//    public static void isNotBlank(String str, String msg) {
//        isNotBlank(str, msg, null);
//    }
//
//    public static void isNotSession (String str, String msg, Level level, Integer code) {
//        if (StringUtils.isBlank(str)) {
//            throw new DescribeException(level, str, msg, code);
//        }
//    }
//
//    public static void isNotBlank(String str, String msg, Level level) {
//        isNotBlank(str, msg, msg, level);
//    }
//
//    /**
//     * @param str
//     * @param msg
//     * @param logMsg
//     *            记录日志的信息
//     * @param level
//     */
//    public static void isNotBlank(String str, String msg, String logMsg, Level level) {
//        if (StringUtils.isBlank(str)) {
//            //throw new SmartException(level, msg, logMsg);
//            throw new DescribeException(level, msg, logMsg);
//        }
//    }
//
//    public static void isBlank(String str, String msg) {
//        if (StringUtils.isNotBlank(str)) {
//            throw new DescribeException(msg);
//        }
//    }
//
//    /**
//     * obj对象不为null，若obj为null则抛异常
//     *
//     * @param obj
//     * @param message
//     *            异常信息
//     */
//    public static void isNotNull(Object obj, String message) {
//        isNotNull(obj, message, null);
//    }
//
//    public static void isNotNull(Object obj, String message, Level level) {
//        isNotNull(obj, message, message, level);
//    }
//
//
//    public static void isNotNull(Object obj, String message, Level level,Integer code) {
//        if (obj == null) {
//            throw new DescribeException(level, message, message,code);
//        }
//    }
//
//    public static void isNotNull(Object obj, String message, String logMessage, Level level) {
//        if (obj == null) {
//            throw new DescribeException(level, message, logMessage);
//        }
//    }
//
//    /**
//     * obj对象为null，若obj为不为null则抛异常
//     *
//     * @param obj
//     * @param message
//     *            异常信息
//     */
//    public static void isNull(Object obj, String message) {
//        if (obj != null) {
//            throw new DescribeException(message);
//        }
//    }
//
//    /**
//     * Assert a boolean expression, throwing {@code IllegalArgumentException} if
//     * the test result is {@code false}.
//     *
//     * <pre class="code">
//     * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
//     * 当flag为false 才会被执行
//     *
//     * @param
//     *
//     * @param message
//     *            the exception message to use if the assertion fails
//     * @throws
//     *             if expression is {@code false}
//     */
//    public static void isTrue(boolean flag, String message) {
//        isTrue(flag, message, null);
//    }
//
//    public static void isTrue(boolean flag, String message, Level level) {
//        isTrue(flag, message, message, level);
//    }
//
//    public static void isTrue(boolean flag, String message, String logMessage, Level level) {
//        if (!flag) {
//            throw new DescribeException(level, message, logMessage);
//        }
//    }
//
//    /**
//     * 当flag为true才会被执行
//     * @param flag
//     * @param message
//     */
//    public static void isFalse(boolean flag, String message) {
//        isFalse(flag, message, null);
//    }
//
//    public static void isFalse(boolean flag, String message, Level level) {
//        isFalse(flag, message, message, level);
//    }
//
//    public static void isFalse(boolean flag, String message, String logMessage, Level level) {
//        if (flag) {
//            throw new DescribeException(level, message, logMessage);
//        }
//    }
//
//    public static <E> void notEmpty(Collection<E> collection, String message) {
//        notEmpty(collection, message, null);
//    }
//
//    public static <E> void notEmpty(Collection<E> collection, String message, Level level) {
//        notEmpty(collection, message, message, level);
//    }
//
//    public static <E> void notEmpty(Collection<E> collection, String message, String logMessage, Level level) {
//        if (CollectionUtils.isEmpty(collection)) {
//            throw new DescribeException(level, message, logMessage);
//        }
//    }
//
//    public static <E> void isEmpty(Collection<E> collection, String message) {
//        isEmpty(collection, message, null);
//    }
//
//    public static <E> void isEmpty(Collection<E> collection, String message, Level level) {
//        isEmpty(collection, message, message, level);
//    }
//
//    public static <E> void isEmpty(Collection<E> collection, String message, String logMessage, Level level) {
//        if (!CollectionUtils.isEmpty(collection)) {
//            throw new DescribeException(level, message, logMessage);
//        }
//    }
//}
