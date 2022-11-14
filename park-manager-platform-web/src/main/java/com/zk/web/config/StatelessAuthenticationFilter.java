package com.zk.web.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessAuthenticationFilter extends AccessControlFilter {

    private SessionConfig sessionConfig;

    public StatelessAuthenticationFilter(SessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if (((HttpServletRequest)servletRequest).getMethod().toUpperCase().equals(HttpMethod.OPTIONS.toString())) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = this.sessionConfig.getToken(request);
        RequestToken requestToken = new RequestToken(token);
        try {
            this.getSubject(servletRequest, servletResponse).login(requestToken);
            return true;
        } catch (AuthenticationException e) {
             response.sendError(HttpStatus.UNAUTHORIZED.value(), "Access is not authorized");
             return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
