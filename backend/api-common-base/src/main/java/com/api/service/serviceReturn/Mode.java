package com.api.service.serviceReturn;

import com.api.service.serializer.SpringRasSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ashy.cheung
 * @date 2020.07.07
 * 微服务之间调用模型
 */

@Data
@ApiModel(value = "Mode", description = "服务交互序列化对象")
public class Mode<T> implements Serializable {
    private byte[] data;
    private Integer code;
    private String msg;

    public static <T> Mode<T> build(T obj) {
        if (null == obj) {
            return null;
            //  throw new RuntimeException("参数不可为空");
        }
        Mode<T> mode = new Mode<>();
        mode.setCode(0);
        mode.setMsg("处理成功");
        mode.setData(SpringRasSerializer.serialize(obj));
        return mode;
    }

    public static <T> Mode<T> build(Integer code, String msg, T obj) {
        Mode<T> mode = new Mode<>();
        mode.setCode(code);
        mode.setMsg(msg);
        if (null != obj) {
            mode.setData(SpringRasSerializer.serialize(obj));
        }

        return mode;
    }

    public T get() {
        if (null != this.data) {
            return (T) SpringRasSerializer.deserialize(this.data);
        } else {
            return null;
        }

    }

    public void errorMsg() {
        if (null != this.code && 0 != this.code) {
            throw new RuntimeException(this.msg);
        }
    }


}
