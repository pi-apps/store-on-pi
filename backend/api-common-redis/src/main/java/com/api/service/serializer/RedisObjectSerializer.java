package com.api.service.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * redis序列化方案 @author Ashy.Cheung
 */
public class RedisObjectSerializer implements RedisSerializer {
    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return null;
        }
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        byte[] bytes = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(t);
            bytes = bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bo) {
                try {
                    bo.close();
                } catch (Exception e1) {

                }
            }
            if (null != oo) {
                try {
                    oo.close();
                } catch (Exception e1) {

                }
            }
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bi) {
                try {
                    bi.close();
                } catch (Exception e1) {

                }
            }
            if (null != oi) {
                try {
                    oi.close();
                } catch (Exception e1) {

                }
            }
        }
        return obj;
    }

}
