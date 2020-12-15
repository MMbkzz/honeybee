package com.stackstech.dcp.server.auth.vo;

/**
 *
 */
public class AuthResponseVo {

    private String returnCode;

    private String returnMessage;

    private String useSso;

    private String loginName;

    public AuthResponseVo(String useSso, String returnCode, String returnMessage, String loginName) {
        this.useSso = useSso;
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.loginName = loginName;
    }

    public AuthResponseVo() {
    }

    public static AuthResponseVo ok(String name) {
        AuthResponseVo vo = new AuthResponseVo();
        vo.setUseSso("true");
        vo.setReturnCode("200");
        vo.setReturnMessage("验证成功");
        vo.setLoginName(name);
        return vo;
    }

    public static AuthResponseVo error(String message) {
        AuthResponseVo vo = new AuthResponseVo();
        vo.setUseSso("true");
        vo.setReturnCode("400");
        vo.setReturnMessage(message);
        return vo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getUseSso() {
        return useSso;
    }

    public void setUseSso(String useSso) {
        this.useSso = useSso;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
