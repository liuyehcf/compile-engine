package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import org.liuyehcf.compile.engine.hua.runtime.Reference;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 计算引用的长度
 * < before → after >
 * < arrayref → value >
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
public class _lengthof extends Compute {
    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x66;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 标志符序号
     */
    private final int order;

    public _lengthof(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public void operate(RuntimeContext context) {
        Reference reference = context.loadReference(order);

        context.push(reference.getSize());

        context.increaseCodeOffset();
    }

    @Override
    public Object[] getOperators() {
        return new Object[]{order};
    }
}
