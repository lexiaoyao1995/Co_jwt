package com.swjtu.shiro;

import com.swjtu.model.User;
import com.swjtu.service.UserService;
import com.swjtu.util.JWTUtil;
import org.apache.shiro.SecurityUtils;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public boolean supports(AuthenticationToken token) {
        if (token instanceof JWTToken || token instanceof UsernamePasswordToken) return true;
        else return false;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        System.out.println("————身份认证方法————");

        System.out.println(authenticationToken instanceof UsernamePasswordToken);
        if (authenticationToken instanceof UsernamePasswordToken) {
            UsernamePasswordToken uptoken = (UsernamePasswordToken) authenticationToken;
            User one = userService.findOneByUsername(uptoken.getUsername());
            if (one == null) return null;
//            ByteSource salt = ByteSource.Util.bytes(uptoken.getUsername());
            return new SimpleAuthenticationInfo("", one.getPassword(), getName());
        }


        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null || !JWTUtil.verify(token, username)) {
            throw new AuthenticationException("token认证失败！");
        }
//        String password = userMapper.getPassword(username);
        User one = userService.findOneByUsername(username);
        if (one == null) return null;
        ByteSource salt = ByteSource.Util.bytes(username);
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

}
