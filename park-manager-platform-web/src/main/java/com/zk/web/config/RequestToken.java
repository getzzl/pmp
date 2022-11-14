package com.zk.web.config;

import org.apache.shiro.authc.AuthenticationToken;

public class RequestToken implements AuthenticationToken {

    private String token;

    public RequestToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this;
    }
}
