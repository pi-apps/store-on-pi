package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_area_info")
@ApiModel(value = "TAreaInfoEntity", description = "数据库-地区表")
public class TAreaInfoEntity extends AbstractPageEntity {

    @TableId("id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String id;

    @TableField("area_code")
    @ApiModelProperty(value = "地区编码", dataType = "String", notes = "地区编码")
    private String areaCode;

    @TableField("area_name")
    @ApiModelProperty(value = "地区名称", dataType = "String", notes = "地区名称")
    private String areaName;

    @TableField("parent_id")
    @ApiModelProperty(value = "父节点", dataType = "String", notes = "父节点")
    private String parentId;

    @TableField("level")
    @ApiModelProperty(value = "层级", dataType = "int", notes = "层级")
    private Integer level;

}
