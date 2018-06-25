package org.liuyehcf.compile.engine.hua.bytecode.oc;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 一维数组创建指令
 * < before → after >
 * < count → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _newarray extends ObjectCreate {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xbc;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 1;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class};

    /**
     * 类型
     * todo 这里应该是一个常量池引用
     */
    private final String type;

    public _newarray(String type) {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate() {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{type};
    }
}
