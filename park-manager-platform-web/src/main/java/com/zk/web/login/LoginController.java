//package com.zk.web.login;
//
//import com.zk.common.domain.system.PmpUser;
//import com.zk.service.login.LoginService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.authz.AuthorizationException;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author: zzl
// * @Date: 2022/11/8 0008
// */
//@RestController
//@Slf4j
//@RequestMapping("/api/auth")
//public class LoginController {
//
//
//    @Autowired
//    LoginService loginService;
//
//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
//
//    @ResponseBody
//    @GetMapping("/login")
//    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
////        PmpUser sysUser = loginService.queryUser(userName);
////        log.info("sysuser: {}", sysUser);
//        // 添加用户认证信息
//
//        Long expireTime = 1000L;
//        redisTemplate.opsForValue().set(userName, password, expireTime, TimeUnit.MINUTES);
//
//        Subject subject = SecurityUtils.getSubject();
//
//        //自己系统的密码加密方式 ,这里简单示例一下MD5
//        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
//        log.info("md5Password :{}",md5Password);
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, md5Password);
//        try {
//            //进行验证，AuthenticationException可以catch到,但是AuthorizationException因为我们使用注解方式,是catch不到的,所以后面使用全局异常捕抓去获取
//            subject.login(usernamePasswordToken);
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//            return "账号或密码错误！";
//        } catch (AuthorizationException e) {
//            e.printStackTrace();
//            return "没有权限";
//        }
//        return "login success";
//    }
//}
