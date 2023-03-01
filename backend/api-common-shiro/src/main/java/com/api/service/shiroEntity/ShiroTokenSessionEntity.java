package com.api.service.shiroEntity;

import lombok.Data;
import org.apache.shiro.session.Session;

import java.io.Serializable;

@Data
public class ShiroTokenSessionEntity implements Serializable {

    private Session session;
    private String token;
}
