package com.api.service.serviceReturn;

import com.api.service.db.mysql.util.PageUtils;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回前端数据
 */
@Data
@ApiModel("R")
public class R<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        if (null != data) {
            if(data instanceof Page){
                Page p = (Page)data;
                this.data = (T)new PageUtils(p);
            }else{
                this.data = data;
            }

        }
    }
}
