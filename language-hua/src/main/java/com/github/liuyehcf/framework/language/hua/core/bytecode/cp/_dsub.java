package com.github.liuyehcf.framework.language.hua.core.bytecode.cp;

import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * double 相减
 * < before → after >
 * < value1, value2 → result >
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class _dsub extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x67;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        double value2 = context.popDouble();
        double value1 = context.popDouble();

        context.push(value1 - value2);

        context.increaseCodeOffset();
    }
}
