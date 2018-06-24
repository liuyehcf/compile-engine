package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 返回整型
 * < before → after >
 * < value → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _ireturn extends InvokeAndReturn {

    public static final int OPERATOR_CODE = 0xac;

    public _ireturn() {
        super(OPERATOR_CODE, 0, null);
    }

    @Override
    public void operate() {

    }
}
