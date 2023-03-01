package com.api.service.db.mysql.util.menu;


import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 树对象
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("MenuTree")
public class MenuTree implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuId;

    private String parentId;

    private String menuName;

    private String menuUrl;

    private String perms;

    private Integer type;

    private String icon;

    private Integer orderNum;

    private String updateId;

    private Date updateTime;

    private String infc;

    private boolean lastLevel = false;

    private List<MenuTree> list;
}