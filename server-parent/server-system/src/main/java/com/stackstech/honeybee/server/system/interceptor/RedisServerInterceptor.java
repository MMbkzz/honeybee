package com.stackstech.honeybee.server.system.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackstech.honeybee.server.system.annotation.ServerCache;
import com.stackstech.honeybee.server.system.util.AnnotationUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedisServerInterceptor {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(com.stackstech.dcp.server.system.annotation.ServerCache)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServerCache annotation = AnnotationUtil.getAnnotation(proceedingJoinPoint, ServerCache.class);

        String key = getCacheKey(proceedingJoinPoint, annotation);

        // 获取已被缓存数据
        Object result = getCacheValue(key, proceedingJoinPoint);
        if (null != result) {
            return result;
        }

        // 执行业务流程
        result = proceedingJoinPoint.proceed();

        // 将返回数据缓存
        setCacheValue(key, result, annotation);
        return result;
    }

    /**
     * 获取需要缓存数据的主键
     *
     * @param proceedingJoinPoint
     * @return
     */
    protected String getCacheKey(ProceedingJoinPoint proceedingJoinPoint, ServerCache annotation) {
        String key = annotation.key();
        Object[] args = proceedingJoinPoint.getArgs();
        if (args.length > 0) {
            key += ":" + args[0];
        }
        return key;
    }

    /**
     * @param key
     * @param proceedingJoinPoint
     * @return
     */
    protected Object getCacheValue(String key, ProceedingJoinPoint proceedingJoinPoint) {
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isBlank(value)) {
                return null;
            }

            Signature sig = proceedingJoinPoint.getSignature();
            MethodSignature methodSignature = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            methodSignature = (MethodSignature) sig;
            Object target = proceedingJoinPoint.getTarget();

            Method method = BeanUtils.findDeclaredMethod(target.getClass(), methodSignature.getName(), methodSignature.getParameterTypes());
            Class clazz = method.getReturnType();

            return objectMapper.readValue(value, clazz);
        } catch (Exception e) {
            logger.error("缓存转换异常", e);
        }

        return null;
    }

    /**
     * @param key
     * @param value
     */
    protected void setCacheValue(String key, Object value, ServerCache annotation) {
        try {
            stringRedisTemplate.opsForValue().append(key, objectMapper.writeValueAsString(value));
            if (annotation.expire() > 0) {
                stringRedisTemplate.expire(key, annotation.expire(), annotation.timeUnit());
            }
        } catch (Exception e) {
            logger.error("缓存写入异常", e);
        }
    }

}
