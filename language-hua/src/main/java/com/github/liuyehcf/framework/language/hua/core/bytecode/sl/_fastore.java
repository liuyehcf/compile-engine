package com.github.liuyehcf.framework.language.hua.core.bytecode.sl;

import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.runtime.HeapMemoryManagement;
import com.github.liuyehcf.framework.language.hua.runtime.Reference;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 存储float型数组元素
 * < before → after >
 * < arrayref, index, value → >
 *
 * @author hechenfeng
 * @date 2018/7/1
 */
public class _fastore extends ArrayStoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x51;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();
        int index = context.pop();
        Reference arrayReference = context.pop();

        int elementOffset = arrayReference.getAddress() + index * Type.FLOAT_TYPE_WIDTH;

        HeapMemoryManagement.storeFloat(elementOffset, value);

        context.increaseCodeOffset();
    }
}

