package com.github.liuyehcf.framework.common.tools.test.asserts;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author hechenfeng
 * @date 2020/2/11
 */
@SuppressWarnings("all")
public class TestAssert {

    private static final String DESCRIPTION = "description";

    @Test
    public void true1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue(false, () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void true2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue(false, DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void true3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue(false),
                null);
    }

    @Test
    public void false1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse(true, () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void false2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse(true, DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void false3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse(true),
                null);
    }

    @Test
    public void notNull1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull(null, () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notNull2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull(null, DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notNull3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull(null),
                null);
    }

    @Test
    public void null1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNull(new Object(), () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void null2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNull(new Object(), DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void null3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNull(new Object()),
                null);
    }

    @Test
    public void nullBlank1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotBlank("", () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void nullBlank2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotBlank("", DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void nullBlank3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotBlank(""),
                null);
    }

    @Test
    public void blank1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertBlank("123", () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void blank2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertBlank("123", DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void blank3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertBlank("123"),
                null);
    }

    @Test
    public void empty1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEmpty(Arrays.asList(1), () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void empty2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEmpty(Arrays.asList(1), DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void empty3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEmpty(Arrays.asList(1)),
                null);
    }

    @Test
    public void notEmpty1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEmpty(Arrays.asList(), () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notEmpty2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEmpty(Arrays.asList(), DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notEmpty3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEmpty(Arrays.asList()),
                null);
    }

    @Test
    public void equal1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEquals(1, 2, () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void equal2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEquals(1, 2, DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void equal3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEquals(1, 2),
                null);
    }

    @Test
    public void notEqual1() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEquals(1, 1, () -> DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notEqual2() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEquals(1, 1, DESCRIPTION),
                DESCRIPTION);
    }

    @Test
    public void notEqual3() {
        test(() -> com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotEquals(1, 1),
                null);
    }

    private void test(Runnable runnable, String description) {
        try {
            runnable.run();
            throw new RuntimeException();
        } catch (Throwable e) {
            Assert.assertTrue(e instanceof IllegalArgumentException || e instanceof NullPointerException);
            Assert.assertEquals(description, e.getMessage());
        }
    }
}
