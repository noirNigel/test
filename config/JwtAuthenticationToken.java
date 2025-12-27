package org.example.demomanagementsystemcproject.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;

    // 未认证的构造函数
    public JwtAuthenticationToken(Object principal, Object credentials) {
        // 明确调用父类的构造函数，传递空的权限集合
        super(Collections.emptyList());
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    // 已认证的构造函数
    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        // 明确调用父类的构造函数，传递权限集合
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // 必须调用父类的方法
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}