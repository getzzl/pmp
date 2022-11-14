package com.zk.web.config.temp;

import com.zk.common.domain.system.User;
import com.zk.common.vo.UserMangerInfo;
import com.zk.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class TokenRealm extends AuthorizingRealm {

    @Autowired
    private SessionConfig sessionConfig;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof RequestToken;
    }

    //授权接口
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token = (String) principalCollection.getPrimaryPrincipal();
        if (token == null) {
            return null;
        }
        Optional<Object> session = this.sessionConfig.getSession(token);
        if (session.isEmpty()) {
            return null;
        }
        User user = (User) session.get();

        UserMangerInfo userMangerInfo = userService.findUserDtoByUserId(user.getUserId());
        log.info("userDto : {}", userMangerInfo);
        if (userMangerInfo == null) {
            return null;
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        log.info("给用户 设置权限 ..... user:{}", user);
        if (!CollectionUtils.isEmpty(userMangerInfo.getRoleMenuDtos())) {
            setPermission(authorizationInfo, userMangerInfo.getRoleMenuDtos());
        }

        return authorizationInfo;
    }


    private void setPermission(SimpleAuthorizationInfo authorizationInfo, Set<UserMangerInfo.RoleMenuDto> roleMenuDtos) {
        if (!CollectionUtils.isEmpty(roleMenuDtos)) {
            roleMenuDtos.stream().forEach(e -> {
                authorizationInfo.addStringPermission(e.getMenuCode());
                if (!CollectionUtils.isEmpty(e.getChild())) {
                    setPermission(authorizationInfo, e.getChild());
                }
            });
        }
    }

    //认证接口
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        RequestToken requestToken = (RequestToken) authenticationToken;
        String token = (String) requestToken.getPrincipal();
        if (token == null) {
            throw new AuthenticationException("Access is not authorized");
        }
        boolean valid = this.sessionConfig.isValid(token);
        if (!valid) {
            throw new AuthenticationException("Invalid authorization");
        }

        return new SimpleAuthenticationInfo(token, requestToken, getName());
    }

}
