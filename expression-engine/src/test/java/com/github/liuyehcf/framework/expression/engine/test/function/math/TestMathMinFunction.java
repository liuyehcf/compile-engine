package com.github.liuyehcf.framework.expression.engine.test.function.math;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathMinFunction extends TestBase {
    @Test
    public void caseLongLong() {
        long result = ExpressionEngine.execute("math.min(1000,10L)");

        assertEquals(Math.min(1000, 10L), result);
    }

    @Test
    public void caseDoubleDouble() {
        double result = ExpressionEngine.execute("math.min(1.0f,1.0001d)");

        assertEquals(Math.min(1.0f, 1.0001d), result, 1e-10);
    }

    @Test
    public void caseLongDouble() {
        double result = ExpressionEngine.execute("math.min(100,1.0001d)");

        assertEquals(Math.min(100, 1.0001d), result, 1e-10);
    }

    @Test
    public void caseDoubleLong() {
        double result = ExpressionEngine.execute("math.min(100.1f,11)");

        assertEquals(Math.min(100.1d, 11), result, 1e-10);
    }
}
