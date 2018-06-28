package org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._lengthof;

/**
 * lengthOf
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
public class LengthOfStatement extends AbstractSemanticAction {

    /**
     * 标志符名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierNameStackOffset;

    public LengthOfStatement(int identifierNameStackOffset) {
        this.identifierNameStackOffset = identifierNameStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String identifierName = context.getAttr(identifierNameStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        Type type = variableSymbol.getType();

        if (!type.isArrayType()) {
            throw new RuntimeException("Operator 'lengthOf' cannot work on non-array type");
        }

        context.addByteCodeToCurrentMethod(new _lengthof(variableSymbol.getOrder()));

        context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT);
    }
}
