package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("AbstractPageEntity")
public abstract class AbstractPageEntity implements Serializable {

    @ApiModelProperty(value = "分页页码", dataType = "Integer", example = "1")
    @TableField(exist = false)
    private Integer page = 1;

    @ApiModelProperty(value = "分页条数", dataType = "Integer", example = "50")
    @TableField(exist = false)
    private Integer limit = 50;

}
