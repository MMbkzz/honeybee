package com.stackstech.honeybee.core.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * @ClassName: ResponseError
 * ResponseError: 服务器错误 500 非正常的捕获
 * ResponseManualError: 手动抛出的错误 200 message error
 * ResponseOk: 成功 200 message success
 */
public class ResponseError {

    public static ResponseEntity<?> create(String msg) {
        ResponseResult error = new ResponseResult();
        error.setCode(400);
        error.setMessage("error: " + msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    public static ResponseEntity<?> create(int code, String msg) {
        ResponseResult error = new ResponseResult();
        error.setCode(code);
        error.setMessage(msg);
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    public static ResponseEntity<?> create(int code, String msg, Object data) {
        ResponseResult error = new ResponseResult();
        error.setCode(code);
        error.setMessage(msg);
        error.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    public static class ResponseResult extends HashMap<String, Object> {
        private static final long serialVersionUID = 1L;

        public String getMessage() {
            return (String) this.get("message");
        }

        public void setMessage(String message) {
            this.put("message", message);
        }

        public int getCode() {
            return (Integer) this.get("code");
        }

        public void setCode(int code) {
            this.put("code", code);
        }

    }

}
