package org.liuyehcf.compile.engine.expression.core.function.collection;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class CollectionSizeFunction extends Function {
    @Override
    public String getName() {
        return "collection.size";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        Object array = arg.getValue();

        if (array == null) {
            return ExpressionValue.valueOf(0);
        }

        if (array.getClass().isArray()) {
            return ExpressionValue.valueOf(Array.getLength(array));
        } else if (array instanceof Collection) {
            return ExpressionValue.valueOf(((Collection) array).size());
        } else {
            throw createTypeIllegalException(1, array);
        }
    }
}
