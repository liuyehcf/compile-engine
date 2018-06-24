package org.liuyehcf.compile.engine.hua.bytecode.cf;

/**
 * 跳转指令，相等时跳转
 * < before → after >
 * < value1, value2 → >
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class _if_icmpeq extends ControlTransfer {

    public static final int OPERATOR_CODE = 0x9f;

    public _if_icmpeq() {
        super(OPERATOR_CODE);
    }

    @Override
    public void operate() {

    }
}