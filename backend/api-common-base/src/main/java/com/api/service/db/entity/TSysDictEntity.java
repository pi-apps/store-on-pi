package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@TableName("t_sys_dict")
@ApiModel(value = "TSysDictEntity",description = "数据库-数据字典表")
public class TSysDictEntity extends AbstractPageEntity{

    private static final long serialVersionUID = 1L;

    @TableId("dict_id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String dictId;

    @TableField("type")
    @ApiModelProperty(value = "类型", dataType = "String", notes = "类型")
    private String type;

    @TableField("label")
    @ApiModelProperty(value = "标签", dataType = "String", notes = "标签")
    private String label;

    @TableField("value")
    @ApiModelProperty(value = "对应值", dataType = "String", notes = "对应值")
    private String value;

    @TableField("status")
    @ApiModelProperty(value = "0正常,1禁用", dataType = "String", notes = "'0正常,1禁用")
    private String status;

    @TableField("level")
    @ApiModelProperty(value = "权重,越小越靠前", dataType = "Integer", notes = "权重,越小越靠前")
    private Integer level;

    @TableField("remarks")
    @ApiModelProperty(value = "备注信息", dataType = "String", notes = "备注信息")
    private String remarks;

    @TableField("p_dict_id")
    @ApiModelProperty(value = "父级ID", dataType = "String", notes = "父级ID")
    private String pDictId;
}
