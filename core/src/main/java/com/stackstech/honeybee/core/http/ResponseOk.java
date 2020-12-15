package com.stackstech.honeybee.core.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @ClassName: ResponseOk
 * ResponseError: 服务器错误 500 非正常的捕获
 * ResponseManualError: 手动抛出的错误 200 message error
 * ResponseOk: 成功 200 message success
 */
public class ResponseOk {

    public static ResponseEntity<?> create(Object result) {
        ResponseResult okResponse = new ResponseResult();
        okResponse.setCode(200);
        okResponse.setMessage("success");
        okResponse.setData(result);
        return ResponseEntity.status(HttpStatus.OK).body(okResponse);
    }

    public static ResponseEntity<?> create(String key, String result) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, result);
        return create(map);
    }

    public static ResponseEntity<?> create(String key, Integer result) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, result);
        return create(map);
    }

    public static ResponseEntity<?> create(String key, Long result) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, result);
        return create(map);
    }

    public static ResponseEntity<?> createCacheTtl(Object result, long ttl) {
        ResponseResult okResponse = new ResponseResult();
        okResponse.setCode(200);
        okResponse.setMessage("success");
        okResponse.setData(result);
        return ResponseEntity.status(HttpStatus.OK).header("X-Cache-TTL", String.valueOf(ttl)).body(okResponse);
    }

    public static class ResponseResult {
        @JsonProperty("code")
        private int code;

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private Object data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }

}
