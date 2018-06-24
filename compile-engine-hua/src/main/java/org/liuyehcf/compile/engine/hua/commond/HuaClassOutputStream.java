package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.compiler.ConstantPool;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
class HuaClassOutputStream extends DataOutputStream {

    HuaClassOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写入中间代码
     */
    void writeHClass(IntermediateInfo intermediateInfo) throws IOException {
        /*
         * 1. 写魔数
         */
        writeString(MAGIC);

        /*
         * 2. 写常量池
         */
        writeConstantPoll(intermediateInfo.getConstantPool());

        /*
         * 3. 写方法表
         */
        writeMethodInfoTable(intermediateInfo.getMethodInfoTable());
    }

    /**
     * 写入常量池
     *
     * @param constantPool 常量池
     */
    private void writeConstantPoll(ConstantPool constantPool) throws IOException {
        /*
         * 写入常量个数
         */
        writeInt(constantPool.getConstants().size());

        for (String constant : constantPool.getConstants()) {

            /*
             * 写常量
             */
            writeString(constant);

        }
    }

    /**
     * 写入方法表
     *
     * @param methodInfoTable 方法表
     */
    private void writeMethodInfoTable(MethodInfoTable methodInfoTable) throws IOException {

        /*
         * 写入方法个数
         */
        writeInt(methodInfoTable.getMethodInfoList().size());

        for (MethodInfo methodInfo : methodInfoTable.getMethodInfoList()) {

            /*
             * 写方法信息
             */
            writeMethodInfo(methodInfo);

        }
    }

    /**
     * 写入方法信息
     *
     * @param methodInfo 方法信息
     */
    private void writeMethodInfo(MethodInfo methodInfo) throws IOException {
        /*
         * 1. 写方法名字
         */
        writeString(methodInfo.getMethodName());

        /*
         * 2. 写返回类型
         */
        writeType(methodInfo.getResultType());

        /*
         * 3. 写方法参数类型列表
         */
        writeInt(methodInfo.getParamTypeList().size());
        for (int i = 0; i < methodInfo.getParamTypeList().size(); i++) {
            writeType(methodInfo.getParamTypeList().get(i));
        }

        /*
         * 4. 写偏移量
         */
        writeInt(methodInfo.getOffset());

        /*
         * 5. 写字节码
         */
        writeInt(methodInfo.getByteCodes().size());
        for (int i = 0; i < methodInfo.getByteCodes().size(); i++) {
            writeByteCode(methodInfo.getByteCodes().get(i));
        }
    }

    private void writeString(String s) throws IOException {
        writeInt(s.length());
        write(s.getBytes());
    }

    private void writeType(Type type) throws IOException {
        /*
         * 1. 写类型名
         */
        writeString(type.getTypeName());

        /*
         * 2. 写类型宽度
         */
        writeInt(type.getTypeWidth());

        /*
         * 3. 写维度
         */
        writeInt(type.getDim());
    }

    private void writeByteCode(ByteCode code) throws IOException {
        int operatorCode = code.getOperatorCode();
        int operatorNum = code.getOperatorNum();
        Class<?>[] operatorClasses = code.getOperatorClasses();

        writeInt(operatorCode);
        writeInt(operatorNum);

    }
}