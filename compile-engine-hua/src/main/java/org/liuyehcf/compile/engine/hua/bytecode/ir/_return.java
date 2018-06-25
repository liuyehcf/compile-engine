package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 返回类型为void的方法的返回指令
 * < before → after >
 * <  → [empty] >
 *
 * @author hechenfeng
 * @date 2018/6/17
 */
public class _return extends Return {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb1;

    public _return() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}
