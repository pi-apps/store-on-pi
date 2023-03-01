package com.api.service.dto;

import com.api.service.db.entity.AbstractPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品检查询
 */
@Data
@ApiModel(value = "StoreServiceSelectEntity")
public class StoreServiceSelectEntity extends AbstractPageEntity {

    @ApiModelProperty(value = "必填:项目名称", dataType = "String")
    private String serviceId;


}
