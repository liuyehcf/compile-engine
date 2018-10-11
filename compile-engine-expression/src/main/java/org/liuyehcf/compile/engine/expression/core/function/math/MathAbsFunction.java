package org.liuyehcf.compile.engine.expression.core.function.math;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class MathAbsFunction extends Function {

    @Override
    public String getName() {
        return "math.abs";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        Object value = arg.getValue();

        if (value instanceof Long) {
            return ExpressionValue.valueOf(Math.abs((long) value));
        } else if (value instanceof Double) {
            return ExpressionValue.valueOf(Math.abs((double) value));
        } else {
            throw createTypeIllegalException(1, value);
        }
    }
}
