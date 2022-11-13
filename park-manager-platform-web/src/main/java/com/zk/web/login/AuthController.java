package com.zk.web.login;

import com.zk.common.constant.SysConstants;
import com.zk.common.domain.system.User;
import com.zk.common.util.EncryptUtils;
import com.zk.service.system.UserService;
import com.zk.web.config.temp.SessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    protected SessionConfig sessionConfig;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {
        log.info("----- 登录请求 -----" + userName);


        Optional<User> user = this.userService.getByUserName(userName);
        User data = user.orElseThrow(() -> new RuntimeException());

        //判断是否删除
        if (SysConstants.DELETE_STATUS_ONE.equals(data.getDeletedStatus())) {
            throw new RuntimeException("用户不存在");
        }

        if (!data.getPassword().equals(EncryptUtils.md5(password))) {
            log.error("Login: " + userName + "; message: 用户名或密码错误");

            throw new RuntimeException("用户名或密码错误");
        }

        String token = SessionConfig.genToken(data);
        this.sessionConfig.setSession(token, data);

        return token;
    }

    @GetMapping("/logout")
    public Void logout(HttpServletRequest request) {
        log.info("----- 注销请求 -----");
        this.sessionConfig.remove(request);
        return null;
    }

}
