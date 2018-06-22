package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.hua.bytecode.oc._anewarray;
import org.liuyehcf.compile.engine.hua.bytecode.oc._multianewarray;
import org.liuyehcf.compile.engine.hua.bytecode.oc._newarray;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

/**
 * 基本类型的数组创建
 *
 * @author chenlu
 * @date 2018/6/22
 */
public class NewPrimaryArray extends AbstractSemanticAction {
    /**
     * 类型-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 维度表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionDimStackOffset;

    /**
     * 空维度-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int emptyDimStackOffset;

    public NewPrimaryArray(int typeStackOffset, int expressionDimStackOffset, int emptyDimStackOffset) {
        this.typeStackOffset = typeStackOffset;
        this.expressionDimStackOffset = expressionDimStackOffset;
        this.emptyDimStackOffset = emptyDimStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type type = context.getStack().get(typeStackOffset).get(AttrName.TYPE.name());
        int expressionDimSize = context.getStack().get(expressionDimStackOffset).get(AttrName.EXPRESSION_DIM_SIZE.name());
        int emptyDimSize = context.getStack().get(emptyDimStackOffset).get(AttrName.EMPTY_DIM_SIZE.name());

        if (expressionDimSize <= 0) {
            throw new RuntimeException("创建数组必须指定第一维的大小");
        }


        assertFalse(type.isArrayType());

        switch (type.getTypeName()) {
            case NORMAL_BOOLEAN:
            case NORMAL_INT:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        Type arrayType = Type.createArrayType(type.getTypeName(), expressionDimSize + emptyDimSize);

        if (expressionDimSize == 1) {
            if (emptyDimSize == 0) {
                /*
                 * 一维数组
                 */
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _newarray(arrayType.toDimDecreasedType().toTypeDescription()));
            } else {
                /*
                 * 多维数组，但是只指定了第一维度
                 */
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _anewarray(arrayType.toDimDecreasedType().toTypeDescription()));
            }
        } else {
            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _multianewarray(arrayType.toTypeDescription(), expressionDimSize));
        }

        context.getLeftNode().put(AttrName.TYPE.name(), arrayType);
    }
}