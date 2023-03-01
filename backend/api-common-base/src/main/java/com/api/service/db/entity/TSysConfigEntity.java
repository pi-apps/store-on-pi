package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_sys_config")
@ApiModel(value = "TSysConfigEntity", description = "数据库-配置表")
public class TSysConfigEntity extends AbstractPageEntity {

    @TableId("key_id")
    @ApiModelProperty(value = "业务key(主键)", dataType = "String")
    private String keyId;

    @TableField("val_str")
    @ApiModelProperty(value = "业务val字符串", dataType = "String")
    private String valStr;

    @TableField("val_text")
    @ApiModelProperty(value = "业务val长字符串", dataType = "String")
    private String valText;

    @TableField("name")
    @ApiModelProperty(value = "名称", dataType = "String")
    private String name;

    @TableField("describe")
    @ApiModelProperty(value = "描述", dataType = "String")
    private String describe;

    @TableField("data_model")
    @ApiModelProperty(value = "数据json模型", dataType = "String")
    private String dataModel;

}
