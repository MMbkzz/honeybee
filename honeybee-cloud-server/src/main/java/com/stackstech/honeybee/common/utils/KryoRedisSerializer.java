package com.stackstech.honeybee.common.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;

/**
 * Kryo redis serializer
 *
 * @author william
 * @since 1.0
 */
@Slf4j
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(Kryo::new);

    private Class<T> clazz;

    public KryoRedisSerializer(Class<T> clazz) {
        super();
        Assert.notNull(clazz, "clazz must not be null!");
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }
        Kryo kryo = kryoThreadLocal.get();
        kryo.setReferences(false);
        kryo.register(clazz);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            kryo.writeClassAndObject(output, t);
            output.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("", e);
        }
        return EMPTY_BYTE_ARRAY;
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        Kryo kryo = kryoThreadLocal.get();
        kryo.setReferences(false);
        kryo.register(clazz);
        try (Input input = new Input(bytes)) {
            return (T) kryo.readClassAndObject(input);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
