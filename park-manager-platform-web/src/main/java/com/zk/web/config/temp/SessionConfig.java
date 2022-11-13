package com.zk.web.config.temp;


import com.zk.common.domain.system.User;
import com.zk.common.dto.UserDto;
import com.zk.common.util.EncryptUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
public class SessionConfig {

    private static final Logger log = LoggerFactory.getLogger(SessionConfig.class);

    @Getter
    @Value("${app.auth.key-of-auth}")
    private String keyOfAuth;

    @Getter
    @Value("${app.auth.expire}")
    private int expireTime;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isValid(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        try {
            Object sessionValue = this.redisTemplate.opsForValue().get(token);
            if (sessionValue != null) {
                this.redisTemplate.expire(token, this.expireTime, TimeUnit.MINUTES);
            }
            return sessionValue != null;
        } catch (Exception e) {
            log.error("Validate session failed", e);
            return false;
        }
    }

    public static String genToken(User user) {
        return EncryptUtils.md5(user.getUserId().toString() + (new Date()).getTime());
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(this.keyOfAuth);
        if (token == null) {
            token = request.getParameter(this.keyOfAuth);
        }
        return token;
    }

    public void setSession(String token, Object data) {
        this.redisTemplate.opsForValue().set(token, data, this.expireTime, TimeUnit.MINUTES);
    }

    public Optional<Object> getSession(HttpServletRequest request) {
        String token = this.getToken(request);
        return this.getSession(token);
    }

    public Optional<UserDto> getSessionUser(HttpServletRequest request) {
        String token = this.getToken(request);
        Optional<Object> session = this.getSession(token);
        return session.map(o -> (UserDto) o);
    }

    public UserDto getSessionUser() {
        HttpServletRequest request = this.getRequest();
        String token = this.getToken(request);
        Optional<Object> session = this.getSession(token);
        return (UserDto) session.orElse(null);
    }

    public Optional<Object> getSession(String token) {
        if (token == null || token.trim().length() == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(token));
    }

    public void remove(HttpServletRequest request) {
        String token = this.getToken(request);
        this.redisTemplate.delete(token);
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

}
