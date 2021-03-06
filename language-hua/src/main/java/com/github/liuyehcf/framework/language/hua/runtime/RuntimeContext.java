package com.github.liuyehcf.framework.language.hua.runtime;

import com.github.liuyehcf.framework.language.hua.core.IntermediateInfo;
import com.github.liuyehcf.framework.language.hua.core.MethodInfo;
import com.github.liuyehcf.framework.language.hua.core.MethodSignature;
import com.github.liuyehcf.framework.language.hua.core.SystemMethod;

/**
 * 运行时上下文
 *
 * @author hechenfeng
 * @date 2018/6/26
 */
public class RuntimeContext {

    /**
     * Hua编译后的中间形式
     */
    private final IntermediateInfo intermediateInfo;

    /**
     * 当前执行的方法
     */
    private MethodRuntimeInfo currentMethod;

    public RuntimeContext(IntermediateInfo intermediateInfo, MethodRuntimeInfo currentMethod) {
        this.intermediateInfo = intermediateInfo;
        this.currentMethod = currentMethod;
    }

    public void push(Object obj) {
        currentMethod.getOperatorStack().push(obj);
    }

    public <T> T pop() {
        return currentMethod.getOperatorStack().pop();
    }

    public long popLong() {
        Object obj = pop();
        /*
         * 做类型兼容
         */
        if (obj instanceof Character) {
            return (long) (char) obj;
        } else if (obj instanceof Integer) {
            return (long) (int) obj;
        }
        return (long) obj;
    }

    public float popFloat() {
        Object obj = pop();
        /*
         * 做类型兼容
         */
        if (obj instanceof Character) {
            return (float) (char) obj;
        } else if (obj instanceof Integer) {
            return (float) (int) obj;
        } else if (obj instanceof Long) {
            return (float) (long) obj;
        }
        return (float) obj;
    }

    public double popDouble() {
        Object obj = pop();
        /*
         * 做类型兼容
         */
        if (obj instanceof Character) {
            return (double) (char) obj;
        } else if (obj instanceof Integer) {
            return (double) (int) obj;
        } else if (obj instanceof Long) {
            return (double) (long) obj;
        } else if (obj instanceof Float) {
            return (double) (float) obj;
        }
        return (double) obj;
    }

    public Object invoke(MethodSignature methodSignature, Object[] args) {
        if (intermediateInfo.getMethodInfoTable().isSystemMethod(methodSignature)) {
            return SystemMethod.invoke(methodSignature, args);
        } else {
            return new MethodRuntimeInfo(intermediateInfo, intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(methodSignature))
                    .run(args);
        }
    }

    public MethodInfo getMethodInfoByMethodSignature(MethodSignature methodSignature) {
        return intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(methodSignature);
    }

    public void normalReturn() {
        currentMethod.finishMethod();
    }

    public void setReturnValue(Object value) {
        setResult(value);
        currentMethod.finishMethod();
    }

    public String getConstant(int constantOffset) {
        return intermediateInfo.getConstantPool().getConstant(constantOffset);
    }

    public void increaseCodeOffset() {
        currentMethod.increaseCodeOffset();
    }

    public void setCodeOffset(int codeOffset) {
        currentMethod.setCodeOffset(codeOffset);
    }

    public int loadInt(int offset) {
        return currentMethod.loadInt(offset);
    }

    public void storeInt(int offset, int value) {
        currentMethod.storeInt(offset, value);
    }

    public long loadLong(int offset) {
        return currentMethod.loadLong(offset);
    }

    public void storeLong(int offset, long value) {
        currentMethod.storeLong(offset, value);
    }

    public float loadFloat(int offset) {
        return currentMethod.loadFloat(offset);
    }

    public void storeFloat(int offset, float value) {
        currentMethod.storeFloat(offset, value);
    }

    public double loadDouble(int offset) {
        return currentMethod.loadDouble(offset);
    }

    public void storeDouble(int offset, double value) {
        currentMethod.storeDouble(offset, value);
    }

    public Reference loadReference(int offset) {
        return currentMethod.loadReference(offset);
    }

    public void storeReference(int offset, Reference reference) {
        currentMethod.storeReference(offset, reference);
    }

    public void setResult(Object result) {
        currentMethod.setResult(result);
    }
}
