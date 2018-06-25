package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 存储加载指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class StoreLoad extends ByteCode {
    StoreLoad(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        super(operatorCode, operatorNum, operatorClasses);
    }
}
