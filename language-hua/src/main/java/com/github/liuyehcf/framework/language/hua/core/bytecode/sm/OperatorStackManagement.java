package com.github.liuyehcf.framework.language.hua.core.bytecode.sm;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ByteCode;

/**
 * 操作数栈管理指令基类
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public abstract class OperatorStackManagement extends ByteCode {

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return OPERATORS;
    }
}
