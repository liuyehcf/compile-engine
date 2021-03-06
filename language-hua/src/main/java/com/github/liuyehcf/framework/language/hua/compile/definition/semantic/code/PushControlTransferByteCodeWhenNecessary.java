package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.code;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.BackFillType;

/**
 * 必要时添加控制转移指令
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class PushControlTransferByteCodeWhenNecessary extends PushControlTransferByteCodeByType {

    /**
     * 包含 expression是否为空 的语法树节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public PushControlTransferByteCodeWhenNecessary(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite, int expressionStackOffset) {
        super(backFillStackOffset, typeStackOffset, backFillType, isOpposite);
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Object object = context.getAttr(expressionStackOffset, AttrName.IS_EMPTY_EXPRESSION);

        if (object != null) {
            return;
        }

        super.onAction(context);
    }
}
