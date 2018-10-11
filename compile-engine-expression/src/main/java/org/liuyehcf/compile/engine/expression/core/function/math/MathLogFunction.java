package org.liuyehcf.compile.engine.expression.core.function.math;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class MathLogFunction extends Function {
    @Override
    public String getName() {
        return "math.log";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        Object value = arg.getValue();

        if (value instanceof Long || value instanceof Double) {
            return ExpressionValue.valueOf(Math.log(((Number) value).doubleValue()));
        } else {
            throw createTypeIllegalException(1, value);
        }
    }
}
