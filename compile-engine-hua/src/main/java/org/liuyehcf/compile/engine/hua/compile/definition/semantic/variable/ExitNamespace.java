package org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 退出当前命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class ExitNamespace extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(HuaContext context) {
        context.exitNamespace();
    }
}