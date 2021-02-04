package com.stackstech.honeybee.server.core.handler;

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

    private static final byte[] EMPTY_ARRAY = new byte[0];

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(Kryo::new);

    private Class<T> clazz;

    private Kryo getKryo() {
        Kryo kryo = kryoThreadLocal.get();
        kryo.setReferences(false);
        kryo.register(clazz);
        return kryo;
    }

    public KryoRedisSerializer(Class<T> clazz) {
        super();
        Assert.notNull(clazz, "clazz must not be null!");
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Output output = new Output(baos)) {
            getKryo().writeClassAndObject(output, t);
            output.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Cannot serialize", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (Input input = new Input(bytes)) {
            return (T) getKryo().readClassAndObject(input);
        } catch (Exception e) {
            throw new SerializationException("Cannot deserialize", e);
        }
    }
}
