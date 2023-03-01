package com.api.service.serializer;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化方案 @author Ashy.Cheung
 */
public class SpringRasSerializer {

    public static byte[] serialize(Object t) {
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


    public static Object deserialize(byte[] bytes)  {
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
