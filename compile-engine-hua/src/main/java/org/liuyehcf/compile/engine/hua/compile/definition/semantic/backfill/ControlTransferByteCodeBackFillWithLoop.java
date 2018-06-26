package org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;

import java.io.Serializable;
import java.util.List;

/**
 * 回填到循环开始处
 *
 * @author hechenfeng
 * @date 2018/6/17
 */
public class ControlTransferByteCodeBackFillWithLoop extends AbstractSemanticAction implements Serializable {
    /**
     * 存储待回填字节码的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public ControlTransferByteCodeBackFillWithLoop(int backFillStackOffset, int loopStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.loopStackOffset = loopStackOffset;
        this.backFillType = backFillType;
    }

    @Override
    public void onAction(HuaContext context) {
        List<ControlTransfer> codes;

        switch (backFillType) {
            case TRUE:
                codes = context.getAttr(backFillStackOffset, AttrName.TRUE_BYTE_CODE);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        int codeOffset = context.getAttr(loopStackOffset, AttrName.CODE_OFFSET);

        for (ControlTransfer code : codes) {
            code.setCodeOffset(codeOffset);
        }
    }
}