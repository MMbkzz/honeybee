package com.stackstech.dcp.core.model;

import com.google.common.collect.Lists;

import java.util.List;

public class LoginUserProtos {

    public static final class LoginUser {

        public LoginUser() {
            loginTime = 0L;
            loginHost = "";
            loginAgent = "";
            loginType = "";
            token = "";
            userId = 0L;
            userName = "";
            roleNames = Lists.newArrayList();
            roleCodes = Lists.newArrayList();
            permissions = Lists.newArrayList();
        }

        private long loginTime;

        private String loginHost;

        private String loginAgent;

        private String loginType;

        private volatile String token;

        private long userId;

        private String userName;

        private List<String> roleNames;
        private List<String> roleCodes;

        private List<String> permissions;

        public long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(long loginTime) {
            this.loginTime = loginTime;
        }

        public String getLoginHost() {
            return loginHost;
        }

        public void setLoginHost(String loginHost) {
            this.loginHost = loginHost;
        }

        public String getLoginAgent() {
            return loginAgent;
        }

        public void setLoginAgent(String loginAgent) {
            this.loginAgent = loginAgent;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<String> getRoleNames() {
            return roleNames;
        }

        public List<String> getRoleCodes() {
            return roleCodes;
        }

        public void setRoleCodes(List<String> roleCodes) {
            this.roleCodes = roleCodes;
        }

        public void setRoleNames(List<String> roleNames) {
            this.roleNames = roleNames;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }
}
