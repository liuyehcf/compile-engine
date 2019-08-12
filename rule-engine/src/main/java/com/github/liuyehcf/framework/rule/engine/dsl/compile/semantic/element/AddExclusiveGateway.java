package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway;

/**
 * @author hechenfeng
 * @date 2019/5/9
 */
public class AddExclusiveGateway extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int GATEWAY_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        ExclusiveGateway exclusiveGateway = context.addExclusiveGateway();

        context.setAttr(GATEWAY_STACK_OFFSET, AttrName.NODE, exclusiveGateway);
    }
}