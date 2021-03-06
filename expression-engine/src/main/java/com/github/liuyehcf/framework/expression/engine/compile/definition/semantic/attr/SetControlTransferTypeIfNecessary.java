package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.attr;


import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class SetControlTransferTypeIfNecessary extends AbstractSemanticAction {

    /**
     * 布尔表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int boolExpressionStackOffset;

    public SetControlTransferTypeIfNecessary(int boolExpressionStackOffset) {
        this.boolExpressionStackOffset = boolExpressionStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        ControlTransferType type = context.getAttr(boolExpressionStackOffset, AttrName.CONTROL_TRANSFER_TYPE);

        if (type == null) {
            context.setAttr(boolExpressionStackOffset, AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFEQ);
        }
    }
}