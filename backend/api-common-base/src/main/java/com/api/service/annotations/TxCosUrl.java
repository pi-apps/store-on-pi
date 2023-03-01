package com.api.service.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TxCosUrl {

    /**
     * 分割字符
     * @return
     */
    String split() default "";

    /**
     * 处理完后填充的字段名称，不填则不填充
     * @return
     */
    String fieldKey() default "";
}
