package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.rpc.ares.constant.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hechenfeng
 * @date 2019/11/11
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AresPathVariable {

    /**
     * path variable name
     */
    String name();

    /**
     * serialize type
     */
    SerializeType serializeType() default SerializeType.fastjson;
}
