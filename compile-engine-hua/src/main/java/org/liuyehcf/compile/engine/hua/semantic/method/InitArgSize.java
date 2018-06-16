package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 初始化参数数量为1
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class InitArgSize extends AbstractSemanticAction {

    /**
     * 参数列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argumentListStackOffset;

    public InitArgSize(int argumentListStackOffset) {
        this.argumentListStackOffset = argumentListStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getStack().get(argumentListStackOffset).put(AttrName.ARGUMENT_SIZE.name(), 1);
    }
}
